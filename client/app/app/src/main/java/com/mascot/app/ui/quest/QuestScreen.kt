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
import com.mascot.app.data.model.Quest
import com.mascot.app.ui.common.QuestItem
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestScreen(navController: NavController, viewModel: QuestViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {

    val quests by viewModel.quests.collectAsState()
    val isGenerating by viewModel.loading.collectAsState()

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

                        val themes = listOf("맛집", "인증샷", "휴식")
                        themes.forEach { theme ->
                            OutlinedButton(
                                onClick = {
                                    println(">>> 버튼 클릭됨! theme = $theme")
                                    selectedTheme = theme
                                    viewModel.requestQuests(theme)
                                    println(">>> requestQuests 호출됨")
                                    showPreferenceDialog = false
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
                        Text(
                            "AI가 '$selectedTheme' 코스를 분석 중입니다...",
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else if (quests.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("아직 받은 의뢰가 없어요.", style = MaterialTheme.typography.titleMedium, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "AR 모드에서 마스코트를 찾으면\n맞춤형 퀘스트가 도착합니다!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.LightGray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(quests) { quest ->
                        QuestItem(quest = quest) { clickedId ->
                            navController.navigate("quest_detail/$clickedId")
                        }
                    }
                }
            }
        }
    }
}
