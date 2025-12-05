package com.mascot.app.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mascot.app.R

@Composable
fun HomeScreen(navController: NavController) {

    // 마스코트 대사 상태
    var mascotTalk by remember {
        mutableStateOf("안녕! 나는 대전의 마스코트 꿈돌이야.\n오늘도 즐거운 여행 해볼까?")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // -----------------------------------------
        // 1. 상단 인사
        // -----------------------------------------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Column {
                Text(
                    text = "대전광역시 ☀️ 맑음",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Text(
                    text = "안녕하세요, 예찬님!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // -----------------------------------------
        // 2. 중앙 말풍선 + 마스코트 영역 (🔥 크게 확장됨)
        // -----------------------------------------
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SpeechBubble(text = mascotTalk)

            Spacer(modifier = Modifier.height(20.dp))

            // 🔥 마스코트 이미지 크기 대폭 확장
            Image(
                painter = painterResource(id = R.drawable.mascot_image),
                contentDescription = "마스코트",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp)   // ← 기존보다 훨씬 큼 (UI 시원해짐)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        mascotTalk = listOf(
                            "으악! 간지러워~ ㅎㅎ",
                            "오늘 성심당 줄이 짧대! 지금이야!",
                            "엑스포 다리 야경은 진짜 최고야.",
                            "배고프다... 빵 사주라..."
                        ).random()
                    },
                contentScale = ContentScale.Fit
            )
        }

        // -----------------------------------------
        // 🔥 3. 오늘의 추천 퀘스트 완전 삭제
        // (텍스트 + RecommendedQuestCard 둘 다 제거)
        // -----------------------------------------

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun SpeechBubble(text: String) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomEnd = 20.dp, bottomStart = 0.dp),
        shadowElevation = 4.dp
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}
