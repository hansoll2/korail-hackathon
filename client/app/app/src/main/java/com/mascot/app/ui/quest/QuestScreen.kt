package com.mascot.app.ui.quest

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mascot.app.data.model.QuestItem
import com.mascot.app.ui.common.QuestItemCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestScreen(
    navController: NavController,
    viewModel: QuestViewModel = viewModel()
) {
    val regions by viewModel.quests.collectAsState()
    val isGenerating by viewModel.loading.collectAsState()

    // 🔹 지역 탭
    val regionTabs = listOf("전체", "서구", "유성구", "중구", "동구", "대덕구")
    var selectedRegion by remember { mutableStateOf("전체") }

    // 🔹 선택된 지역 퀘스트
    val displayedQuests: List<QuestItem> = remember(regions, selectedRegion) {
        if (selectedRegion == "전체") {
            regions.values.flatten()
        } else {
            regions[selectedRegion].orEmpty()
        }
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate("tutorial_start") },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("튜토리얼 시작하기")
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {

            /* ---------- 🔥 상단 타이틀 + 완료된 퀘스트 버튼 ---------- */
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "퀘스트 목록",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                TextButton(
                    onClick = {
                        navController.navigate("completed_quests")
                    }
                ) {
                    Text("완료된 퀘스트")
                }
            }

            /* ---------- 지역 선택 ---------- */
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                regionTabs.forEach { region ->
                    FilterChip(
                        selected = selectedRegion == region,
                        onClick = { selectedRegion = region },
                        label = { Text(region) }
                    )
                }
            }

            when {
                // 1) 생성 중
                isGenerating -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("퀘스트 생성 중입니다…")
                        }
                    }
                }

                // 2) 없음
                displayedQuests.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("아직 받은 의뢰가 없어요.", color = Color.Gray)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "튜토리얼을 시작하면\n맞춤형 퀘스트가 도착합니다!",
                                color = Color.LightGray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // 3) 리스트
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(displayedQuests) { quest ->
                            QuestItemCard(
                                quest = quest,
                                onClick = { id ->
                                    navController.navigate("quest_detail/$id")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
