package com.mascot.app.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.mascot.app.ui.Screen

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    // 상태 구독
    val homeState by viewModel.screenState.collectAsState()
    val objects by viewModel.unlockedObjects.collectAsState()
    val questCount by viewModel.questCount.collectAsState()
    val showRafflePopup by viewModel.showRafflePopup.collectAsState()

    // 🧪 테스트용 팝업 상태
    var showTestPopup by remember { mutableStateOf(false) }

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (homeState) {

                HomeState.LOCKED -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // 🧪 테스트 버튼
                        Button(onClick = { showTestPopup = true }) {
                            Text("🧪 팝업 테스트")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        HomeLockedScreen(
                            onGoToAR = {
                                navController.navigate(Screen.AR.route) {
                                    popUpTo(navController.graph.findStartDestination().id)
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                }

                HomeState.FIRST_ENTER -> {
                    NewFriendPopup(
                        onDismiss = { viewModel.finishFirstEnter() },
                        onGoToQuest = {
                            viewModel.finishFirstEnter()
                            navController.navigate(Screen.Quest.route)
                        }
                    )
                }

                HomeState.ROOM -> {
                    MascotRoom(
                        objects = objects,
                        onQuestTest = { viewModel.debugProgressQuest() }
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 60.dp)
                    ) {
                        QuestProgressUI(
                            current = questCount,
                            total = 3,
                            onHeaderClick = {
                                if (questCount < 3) {
                                    viewModel.debugProgressQuest()
                                } else {
                                    viewModel.openRafflePopup()
                                }
                            }
                        )
                    }
                }
            }

            // 래플 팝업
            if (showRafflePopup) {
                RaffleTicketPopup(
                    onDismiss = { viewModel.closeRafflePopup() }
                )
            }

            // 🧪 테스트용 팝업 호출
            if (showTestPopup) {
                NewFriendPopup(
                    onDismiss = { showTestPopup = false },
                    onGoToQuest = {
                        showTestPopup = false
                        navController.navigate(Screen.Quest.route)
                    }
                )
            }
        }
    }
}

@Composable
fun QuestProgressUI(
    current: Int,
    total: Int,
    onHeaderClick: () -> Unit
) {
    val isComplete = current >= total

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "퀘스트 진행도",
            fontSize = 30.sp,
            color = Color.DarkGray,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            color = if (isComplete) Color(0xFFFFE082) else Color.White.copy(alpha = 0.8f),
            shape = RoundedCornerShape(20.dp),
            shadowElevation = if (isComplete) 4.dp else 0.dp,
            onClick = { onHeaderClick() }
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinearProgressIndicator(
                    progress = if (total > 0) current / total.toFloat() else 0f,
                    modifier = Modifier
                        .width(100.dp)
                        .height(8.dp),
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
