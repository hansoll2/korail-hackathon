package com.mascot.app.ui.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutQuad
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mascot.app.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun MascotRoom(
    objects: List<String>,
    onQuestTest: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // 1. 전체 배경 이미지 (화면에 꽉 차게)
        Image(
            painter = painterResource(id = R.drawable.bg_room_final), // 새로 가져온 배경
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 2. 상단: 퀘스트 진척도 (배치도 위치 참고)
            Spacer(modifier = Modifier.height(60.dp)) // 상단 여백 조절

            Spacer(modifier = Modifier.weight(1f)) // 중간 여백

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                // (1) 꿈돌이 본체 (위치 y=160dp 유지)
                Box(
                    modifier = Modifier.offset(y = 160.dp)
                ) {
                    JumpingMascot()
                }


                // (2) 획득한 오브제들 (개별 위치 & 개별 크기 설정)
                objects.forEachIndexed { index, objName ->

                    // index(순서)에 따라 -> (X위치, Y위치, 크기)를 다르게 설정!
                    val (offsetX, offsetY, customSize) = when (index) {

                        0 -> Triple((-10).dp, 290.dp, 90.dp)
                        1 -> Triple(130.dp, 170.dp, 200.dp)
                        2 -> Triple((-130).dp, 170.dp, 200.dp)

                        else -> Triple(0.dp, 0.dp, 100.dp)
                    }

                    Image(
                        painter = painterResource(id = getObjectResId(objName)),
                        contentDescription = objName,
                        modifier = Modifier
                            .size(customSize)
                            .offset(x = offsetX, y = offsetY)
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1.5f)) // 하단 여백 (네비게이션 바 공간 확보)
        }
    }
}

@Composable
fun JumpingMascot() {

    val mascotPoses = listOf(
        R.drawable.kum1,
        R.drawable.kum2,
        R.drawable.kum3,
        R.drawable.kum4,
        R.drawable.kum5
    )


    var currentIndex by remember { mutableIntStateOf(0) }

    val offsetY = remember { Animatable(0f) }

    // 애니메이션 루프
    LaunchedEffect(Unit) {
        while (true) {
            delay(1500)

            launch {

                offsetY.animateTo(
                    targetValue = -60f,
                    animationSpec = tween(durationMillis = 350, easing = EaseOutQuad)
                )

                offsetY.animateTo(
                    targetValue = 0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }

            delay(300)
            currentIndex = (currentIndex + 1) % mascotPoses.size

            delay(600)
        }
    }

    Box(contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(id = mascotPoses[currentIndex]),
            contentDescription = "꿈돌이",
            modifier = Modifier
                .size(200.dp)
                .offset { IntOffset(x = 0, y = offsetY.value.roundToInt()) }
        )
    }
}

fun getObjectResId(name: String): Int {
    return when (name) {
        "튀김소보로" -> R.drawable.soboro
        "한빛탑" -> R.drawable.hanbit
        "대전오월드" -> R.drawable.oworld
        else -> R.drawable.ic_launcher_foreground
    }
}
