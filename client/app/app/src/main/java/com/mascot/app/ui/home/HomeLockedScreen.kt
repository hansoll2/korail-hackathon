package com.mascot.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.shape.RoundedCornerShape
import com.mascot.app.R

@Composable
fun HomeLockedScreen(onGoToAR: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        // 🔹 방 배경 + 블러 처리
        Image(
            painter = painterResource(id = R.drawable.room_background_light),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .blur(30.dp),   // 🔥 블러 강도
            alpha = 0.6f
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                "아직 마스코트를 발견하지 않았어요!",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "AR 모드에서 주변을 스캔해\n마스코트를 찾아보세요!",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 🔹 AR 이동 버튼
            Button(
                onClick = onGoToAR,
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth(0.6f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("AR 모드로 이동", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
