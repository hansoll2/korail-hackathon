package com.mascot.app.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mascot.app.R

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

            QuestProgressUI(
                current = objects.size,
                total = 3,
                onHeaderClick = onQuestTest
            )

            Spacer(modifier = Modifier.weight(1f)) // 중간 여백

            // 3. 중앙: 마스코트 및 오브제
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                // 꿈돌이 캐릭터
                Image(
                    painter = painterResource(id = R.drawable.char_kumdori),
                    contentDescription = "Kumdori",
                    modifier = Modifier
                        .size(200.dp) // 캐릭터 크기 조절
                        .offset(y = 160.dp) // 바닥 그림에 맞춰 위치 내리기 (값 조절 필요)
                )

                // 획득한 오브제 (캐릭터 발 밑에 배치)
                /* 오브제 이미지 준비되면 주석 해제
                if (objects.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 40.dp), // 바닥 높이 조절
                        horizontalArrangement = Arrangement.Center
                    ) {
                        objects.forEach { obj ->
                            Image(
                                painter = painterResource(id = getObjectResId(obj)),
                                contentDescription = null,
                                modifier = Modifier.size(50.dp).padding(horizontal = 4.dp)
                            )
                        }
                    }
                }
                */
            }

            Spacer(modifier = Modifier.weight(1.5f)) // 하단 여백 (네비게이션 바 공간 확보)
        }
    }
}

// 🔹 퀘스트 진척도 UI (이전과 동일)
@Composable
fun QuestProgressUI(current: Int, total: Int, onHeaderClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "QUEST PROGRESS",
            fontSize = 12.sp,
            color = Color.DarkGray, // 배경에 맞춰 글씨색 진하게
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { onHeaderClick() }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            color = Color.White.copy(alpha = 0.8f), // 배경이 살짝 비치게 반투명 처리
            shape = RoundedCornerShape(20.dp),
            shadowElevation = 0.dp // 그림자 제거 (깔끔하게)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinearProgressIndicator(
                    progress = { if (total > 0) current / total.toFloat() else 0f },
                    modifier = Modifier.width(100.dp).height(8.dp),
                    color = Color(0xFFFFD260),
                    trackColor = Color(0xFFEEEEEE),
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "$current / $total",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
            }
        }
    }
}

// 오브제 ID 헬퍼 (필요시 주석 해제)
/*
fun getObjectResId(name: String): Int {
    return when (name) {
        "튀김소보로" -> R.drawable.obj_soboro
        else -> R.drawable.obj_soboro
    }
}
*/