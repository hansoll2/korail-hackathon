package com.mascot.app.ui.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape // 추가됨
import androidx.compose.material3.Surface // 추가됨
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mascot.app.R

@Composable
fun HomeLockedScreen(
    onGoToAR: () -> Unit
) {
    // 상자 두근거림 애니메이션
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1.05f,
        animationSpec = infiniteRepeatable(animation = tween(1000), repeatMode = RepeatMode.Reverse),
        label = "scale"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. 전체 배경 (꽉 차게)
        Image(
            painter = painterResource(id = R.drawable.bg_room_final),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(120.dp))

            Surface(
                color = Color.White.copy(alpha = 0.9f),
                shape = RoundedCornerShape(20.dp),
                shadowElevation = 2.dp
            ) {
                Text(
                    text = "이 상자 안에 누가 있을까?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // 2. 중앙: 미스터리 상자
            Image(
                painter = painterResource(id = R.drawable.img_mystery_box),
                contentDescription = "Box",
                modifier = Modifier
                    .size(160.dp)
                    .scale(scale)
                    .offset(y = 150.dp) // ✨ 아까 요청하신 대로 바닥 쪽으로 내림
                    .clickable { onGoToAR() }
            )

            Spacer(modifier = Modifier.weight(1.5f))
        }
    }
}