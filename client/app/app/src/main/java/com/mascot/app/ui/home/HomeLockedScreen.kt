package com.mascot.app.ui.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mascot.app.R

@Composable
fun HomeLockedScreen(
    onGoToAR: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1.05f,
        animationSpec = infiniteRepeatable(animation = tween(1000), repeatMode = RepeatMode.Reverse),
        label = "scale"
    )

    Box(modifier = Modifier.fillMaxSize()) {
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
            Spacer(modifier = Modifier.weight(1f))

            Surface(
                color = Color.White.copy(alpha = 0.9f),
                shape = RoundedCornerShape(20.dp),
                shadowElevation = 2.dp
            ) {
                Text(
                    text = "두근두근\n어떤 친구가 있을까요?",
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 2. 중앙: 미스터리 상자
            Image(
                painter = painterResource(id = R.drawable.img_mystery_box),
                contentDescription = "Box",
                modifier = Modifier
                    .size(300.dp)
                    .scale(scale)
                    .offset(y = 50.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = onGoToAR,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)), // 주황색 포인트
                shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth(0.7f) // 너비 70%
                    .height(56.dp)
            ) {
                Text(
                    text = "AR로 찾으러 가기",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}