package com.mascot.app.ui.quest

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mascot.app.R
import com.mascot.app.data.model.Quest
import com.mascot.app.ui.home.QuestItem
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestScreen(navController: NavController) {
    // 받은 퀘스트 목록
    val receivedQuests = remember { mutableStateListOf<Quest>() }
    var isGenerating by remember { mutableStateOf(false) }

    // 다이얼로그 상태
    var showPreferenceDialog by remember { mutableStateOf(false) }
    var selectedTheme by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showPreferenceDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("AR 수집 (시뮬레이션)")
            }
        }
    ) { innerPadding ->

        // 1. 취향 선택 팝업
        if (showPreferenceDialog) {
            AlertDialog(
                onDismissRequest = { showPreferenceDialog = false },
                title = { Text(text = "어떤 모험을 떠날까요?") },
                text = {
                    Column {
                        Text("마스코트가 맞춤형 퀘스트를 준비합니다.\n원하는 테마를 골라주세요!")
                        Spacer(modifier = Modifier.height(16.dp))

                        val themes = listOf("🍔 맛집 탐방", "📸 인증샷 명소", "☕ 힐링/휴식")
                        themes.forEach { theme ->
                            OutlinedButton(
                                onClick = {
                                    selectedTheme = theme
                                    showPreferenceDialog = false
                                    isGenerating = true
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(theme)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                },
                confirmButton = {}
            )
        }

        // 2. AI 생성 로직 (URL 이미지 적용됨!)
        LaunchedEffect(isGenerating) {
            if (isGenerating) {
                delay(1500)

                // ★ 테마별 데이터 생성 (이미지 URL 사용)
                val (title, desc, imgUrl) = when(selectedTheme) {
                    "🍔 맛집 탐방" -> Triple(
                        "대전역 가락국수 먹기",
                        "출출하시죠? 대전역의 명물 가락국수 한 그릇 어때요?",
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c5/Sungsimdang_Bakery_Daejeon_Station_Branch.jpg/640px-Sungsimdang_Bakery_Daejeon_Station_Branch.jpg" // 임시로 성심당 사진 사용
                    )
                    "📸 인증샷 명소" -> Triple(
                        "꽃시계 앞 인증샷",
                        "대전역 광장 꽃시계 앞에서 마스코트와 사진을 찍으세요!",
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/8/82/Daejeon_Station_20180915.jpg/640px-Daejeon_Station_20180915.jpg" // 대전역 사진
                    )
                    "☕ 힐링/휴식" -> Triple(
                        "소제동 카페거리 산책",
                        "여행의 피로를 풀 수 있는 조용한 카페를 찾았어요.",
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e3/Expo_Bridge.jpg/640px-Expo_Bridge.jpg" // 엑스포 다리 사진
                    )
                    else -> Triple(
                        "마스코트와의 산책",
                        "주변을 가볍게 걸어볼까요?",
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/8/82/Daejeon_Station_20180915.jpg/640px-Daejeon_Station_20180915.jpg"
                    )
                }

                val newQuest = Quest(
                    id = receivedQuests.size + 1,
                    title = title,
                    description = desc,
                    imageUrl = imgUrl, // 👈 URL 잘 들어갔습니다!
                    isCompleted = false
                )
                receivedQuests.add(0, newQuest)
                isGenerating = false
            }
        }

        // 3. 화면 UI
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "받은 의뢰함 📩",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (isGenerating) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("AI가 '${selectedTheme}' 코스를\n분석 중입니다...", textAlign = TextAlign.Center)
                    }
                }
            } else if (receivedQuests.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("아직 받은 의뢰가 없어요.", style = MaterialTheme.typography.titleMedium, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("AR 모드에서 마스코트를 찾으면\n맞춤형 퀘스트가 도착합니다!", style = MaterialTheme.typography.bodyMedium, color = Color.LightGray, textAlign = TextAlign.Center)
                    }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(receivedQuests) { quest ->
                        QuestItem(quest = quest) { clickedId ->
                            navController.navigate("quest_detail/$clickedId")
                        }
                    }
                }
            }
        }
    }
}