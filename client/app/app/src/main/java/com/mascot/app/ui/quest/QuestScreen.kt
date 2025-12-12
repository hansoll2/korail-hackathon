package com.mascot.app.ui.quest

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.navigation.NavController
import com.mascot.app.ui.Screen
import com.mascot.app.ui.common.QuestItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestScreen(navController: NavController, viewModel: QuestViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {

    val quests by viewModel.quests.collectAsState()
    val isGenerating by viewModel.loading.collectAsState()

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    navController.navigate("tutorial_start")                },
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
            Text(
                text = "받은 의뢰함 📩",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            when {
                isGenerating -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("AI가 여행을 준비 중입니다...", textAlign = TextAlign.Center)
                        }
                    }
                }

                quests.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("아직 받은 의뢰가 없어요.", color = Color.Gray)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("튜토리얼을 시작하면\n맞춤형 퀘스트가 도착합니다!",
                                color = Color.LightGray,
                                textAlign = TextAlign.Center)
                        }
                    }
                }

                else -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(quests) { quest ->
                            QuestItem(quest = quest) { id ->
                                navController.navigate("quest_detail/$id")
                            }
                        }
                    }
                }
            }
        }
    }
}
