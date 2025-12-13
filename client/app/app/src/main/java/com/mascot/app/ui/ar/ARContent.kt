package com.mascot.app.ui.ar

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.media.Image
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.ar.core.*
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

@Composable
fun ARContent(
    viewModel: ARViewmodel = hiltViewModel(), // 클래스명 대소문자 확인 (ARViewmodel vs ARViewModel)
    onCollectionFinished: () -> Unit
) {
    val context = LocalContext.current
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)
    val childNodes = rememberNodes()
    val scope = rememberCoroutineScope()
    val textRecognizer = remember { TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build()) }

    var isModelPlaced by remember { mutableStateOf(false) }
    var debugMessage by remember { mutableStateOf("카메라로 '대전' 글자를 찾아보세요") }
    var isProcessing by remember { mutableStateOf(false) }

    // 스로틀링: 마지막 인식 시간 저장 변수
    var lastProcessTime by remember { mutableStateOf(0L) }

    Box(modifier = Modifier.fillMaxSize()) {
        ARScene(
            modifier = Modifier.fillMaxSize(),
            childNodes = childNodes,
            engine = engine,
            modelLoader = modelLoader,
            sessionConfiguration = { _, config ->
                config.focusMode = Config.FocusMode.AUTO
                config.planeFindingMode = Config.PlaneFindingMode.HORIZONTAL_AND_VERTICAL
                config.lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
            },
            onSessionUpdated = { session, frame ->
                val currentTime = System.currentTimeMillis()

                // 텍스트 인식 & 배치 로직 (0.5초 쿨타임)
                if (!isModelPlaced && !isProcessing &&
                    frame.camera.trackingState == TrackingState.TRACKING &&
                    (currentTime - lastProcessTime > 500)
                ) {
                    val image = try { frame.acquireCameraImage() } catch (e: Exception) { null }
                    if (image != null) {
                        isProcessing = true
                        lastProcessTime = currentTime

                        // 중앙 크롭 (Crop) 적용
                        val croppedBitmap = cropCenterBitmap(image)
                        image.close()

                        if (croppedBitmap != null) {
                            val inputImage = InputImage.fromBitmap(croppedBitmap, 90)

                            textRecognizer.process(inputImage).addOnSuccessListener { text ->
                                if (text.text.contains("대전")) {
                                    // [배치 전략]
                                    val centerX = frame.camera.imageIntrinsics.principalPoint[0]
                                    val centerY = frame.camera.imageIntrinsics.principalPoint[1]
                                    val hits = frame.hitTest(centerX, centerY)
                                    val planeHit = hits.firstOrNull { it.trackable is Plane && (it.trackable as Plane).isPoseInPolygon(it.hitPose) }

                                    val anchor = if (planeHit != null) {
                                        debugMessage = "평면 인식 성공! (바닥/벽에 배치)"
                                        planeHit.createAnchor()
                                    } else {
                                        debugMessage = "공중 배치 (카메라 앞 50cm)"
                                        val camPose = frame.camera.pose
                                        val zAxis = camPose.zAxis
                                        session.createAnchor(Pose(
                                            floatArrayOf(camPose.tx() - zAxis[0]*0.5f, camPose.ty() - zAxis[1]*0.5f, camPose.tz() - zAxis[2]*0.5f),
                                            floatArrayOf(0f, 0f, 0f, 1f)
                                        ))
                                    }

                                    val anchorNode = AnchorNode(engine, anchor)
                                    scope.launch {
                                        val instance = modelLoader.createModelInstance("mascot.glb")
                                        val modelNode = ModelNode(instance, scaleToUnits = 0.3f).apply {
                                            parent = anchorNode

                                            // 1. 카메라 바라보기
                                            val camPosition = Position(frame.camera.pose.tx(), frame.camera.pose.ty(), frame.camera.pose.tz())
                                            lookAt(camPosition)

                                            // 2. 뒤돌아 있다면 180도 회전
                                            rotation = Rotation(rotation.x, rotation.y + 180f, rotation.z)

                                            // 3. 터치 이벤트
                                            onSingleTapConfirmed = {
                                                Toast.makeText(context, "🎉 마스코트 수집 완료!", Toast.LENGTH_SHORT).show()

                                                // DB 저장 요청
                                                val detectedMascotId = 1001
                                                viewModel.onMascotCollected(detectedMascotId)

                                                onCollectionFinished()

                                                true
                                            }
                                        }
                                        childNodes.add(anchorNode)
                                        isModelPlaced = true
                                    }
                                }
                            }.addOnCompleteListener {
                                isProcessing = false
                            }
                        } else {
                            isProcessing = false
                        }
                    }
                }
            }
        )

        // 안내 텍스트
        Text(
            text = debugMessage,
            color = Color.White,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 120.dp)
        )
    }
}

// YUV 이미지를 비트맵으로 변환하고 중앙을 자르는 함수
fun cropCenterBitmap(image: Image): Bitmap? {
    return try {
        val yBuffer = image.planes[0].buffer
        val uBuffer = image.planes[1].buffer
        val vBuffer = image.planes[2].buffer

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)

        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
        val out = ByteArrayOutputStream()

        val cropWidth = image.width / 2
        val cropHeight = image.height / 2
        val left = (image.width - cropWidth) / 2
        val top = (image.height - cropHeight) / 2
        val rect = Rect(left, top, left + cropWidth, top + cropHeight)

        yuvImage.compressToJpeg(rect, 100, out)
        val imageBytes = out.toByteArray()
        BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}