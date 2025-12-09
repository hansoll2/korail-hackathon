package com.mascot.app.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mascot.app.R

@Composable
fun MascotRoom(
    objects: List<RoomObject>
) {
    Box(modifier = Modifier.fillMaxSize()) {

        // 🔹 배경
        Image(
            painter = painterResource(id = R.drawable.room_background_light),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            alpha = 1f
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🔹 말풍선
            SpeechBubble(text = "오늘도 좋은 하루 보내자!")

            Spacer(modifier = Modifier.height(20.dp))

            // 🔹 마스코트
            Image(
                painter = painterResource(id = R.drawable.mascot_image),
                contentDescription = null,
                modifier = Modifier.size(280.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // 🔹 오브젝트 영역
            if (objects.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    objects.forEach { obj ->
                        Image(
                            painter = painterResource(id = obj.resId),
                            contentDescription = obj.name,
                            modifier = Modifier
                                .size(80.dp)
                                .padding(horizontal = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
