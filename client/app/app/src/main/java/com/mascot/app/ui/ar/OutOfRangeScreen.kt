package com.mascot.app.ui.ar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalDensity
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun OutOfRangeScreen(
    currentLat: Double,
    currentLon: Double,
    targetLat: Double,
    targetLon: Double,
    currentDist: Float,
    onRefreshLocation: () -> Unit
) {
    // 1. 목표 지점 좌표
    val targetLocation = LatLng(targetLat, targetLon)

    // 2. 카메라 위치 상태 (초기 위치: 목표 지점, 줌 14)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(targetLocation, 14f)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // 3. 구글 지도 컴포저블
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = true, // 내 위치 파란 점 표시
                mapType = MapType.NORMAL
            ),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false, // 줌 버튼 숨김
                myLocationButtonEnabled = false // (GoogleMap 속성명) 내 위치 버튼 숨김
            )
        ) {
            // 목표 지점 마커
            Marker(
                state = MarkerState(position = targetLocation),
                title = "목표 지점",
                snippet = "여기 100m 안으로 오세요"
            )

            // 목표 반경
            val strokeWidthPx = with(LocalDensity.current) { 2.dp.toPx() }

            Circle(
                center = targetLocation,
                radius = 100.0, // 미터 단위
                fillColor = Color(0x334E7AFF),
                strokeColor = Color(0xFF4E7AFF),
                strokeWidth = strokeWidthPx
            )
        }

        // 4. 하단 탭 뷰
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                // 상단 타이틀
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "위치 정보 확인",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 설명
                Text(
                    text = "현재 위치가 목표 범위 밖에 있습니다.",
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 거리 정보
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(text = "목표까지 ", fontSize = 16.sp, color = Color.Gray)
                    Text(
                        text = "${currentDist.toInt()}m",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4E7AFF)
                    )
                    Text(text = " 남았습니다.", fontSize = 16.sp, color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(30.dp))

                // 하단 버튼
                Button(
                    onClick = onRefreshLocation,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF536DFE))
                ) {
                    Text(
                        text = "내 위치 새로고침",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}