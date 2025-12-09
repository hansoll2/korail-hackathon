package com.mascot.app.ui.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mascot.app.R
import kotlinx.coroutines.delay
import androidx.compose.foundation.background

@Composable
fun MascotMeetAnimation(onFinish: () -> Unit) {

    var play by remember { mutableStateOf(true) }

    // 🔹 스케일 애니메이션
    val scale by animateFloatAsState(
        targetValue = if (play) 1.2f else 1f,
        animationSpec = tween(800, easing = EaseOutBack)
    )

    LaunchedEffect(Unit) {
        delay(1300)
        play = false
        delay(1000)
        onFinish()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xCC000000)), // 반투명 어두운 배경
        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.mascot_image), // 꿈돌이 이미지
            contentDescription = null,
            modifier = Modifier
                .size(260.dp)
                .scale(scale)
        )

        Text(
            text = "만나서 반가워! 난 꿈돌이야!",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 120.dp)
        )
    }
}
