package com.mascot.app.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import coil.compose.AsyncImage // Coil 필수 임포트
import com.mascot.app.R
import com.mascot.app.data.model.Quest

@Composable
fun HomeScreen(navController: NavController) {
    // 마스코트 대사 상태 관리
    var mascotTalk by remember { mutableStateOf("안녕! 나는 대전의 마스코트 꿈돌이야.\n오늘도 즐거운 여행 해볼까?") }

    // 오늘의 추천 퀘스트 데이터 (수정됨: imageUrl 사용)
    val todayQuest = Quest(
        id = 1,
        title = "성심당 튀김소보로 구매",
        description = "대전의 명물! 갓 튀긴 튀김소보로를 맛보세요.",
        // ▼▼▼ 실제 인터넷 주소로 변경 ▼▼▼
        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c5/Sungsimdang_Bakery_Daejeon_Station_Branch.jpg/640px-Sungsimdang_Bakery_Daejeon_Station_Branch.jpg",
        isCompleted = false
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. 상단: 날씨 및 인사
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

        Spacer(modifier = Modifier.height(40.dp))

        // 2. 중앙: 마스코트 & 말풍선 (여기는 앱 내장 이미지 유지)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                SpeechBubble(text = mascotTalk)

                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    painter = painterResource(id = R.drawable.mascot_image),
                    contentDescription = "마스코트",
                    modifier = Modifier
                        .size(280.dp)
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
        }

        // 3. 하단: 오늘의 추천 퀘스트
        Text(
            text = "🚀 오늘의 강력 추천",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        RecommendedQuestCard(quest = todayQuest) {
            navController.navigate("quest_detail/${todayQuest.id}")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// 말풍선 디자인 컴포넌트
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

// 추천 퀘스트 카드 디자인 (수정됨: AsyncImage)
@Composable
fun RecommendedQuestCard(quest: Quest, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(100.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 썸네일 이미지 (AsyncImage 사용)
            AsyncImage(
                model = quest.imageUrl, // URL 사용
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.mascot_image),
                error = painterResource(id = R.drawable.mascot_image)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // 텍스트 정보
            Column {
                Text(text = quest.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = "바로 가기 >", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

// 퀘스트 아이템 (수정됨: AsyncImage + 사이즈 복구)
@Composable
fun QuestItem(quest: Quest, onItemClick: (Int) -> Unit) {
    Card(
        onClick = { onItemClick(quest.id) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. 이미지 (인터넷 이미지 로딩)
            AsyncImage(
                model = quest.imageUrl, // URL 사용
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp) // 크기 지정 필수!
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.mascot_image), // 로딩 중 보여줄 것
                error = painterResource(id = R.drawable.mascot_image),       // 에러 시 보여줄 것
                alpha = if (quest.isCompleted) 1.0f else 0.4f // 미수집 시 흐리게
            )

            Spacer(modifier = Modifier.width(16.dp))

            // 2. 텍스트 정보
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = quest.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (quest.isCompleted) "수집 완료! 🎉" else "마스코트 찾는 중... 🕵️",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (quest.isCompleted) Color.Blue else Color.Gray
                )
            }

            // 3. 화살표 아이콘
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_send),
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}