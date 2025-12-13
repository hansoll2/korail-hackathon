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

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    // 1. 뷰모델 상태 구독
    val homeState by viewModel.screenState.collectAsState()
    val objects by viewModel.unlockedObjects.collectAsState()

    // 퀘스트 진행도 (0 ~ 3)
    val questCount by viewModel.questCount.collectAsState()

    // 래플 팝업 표시 여부
    val showRafflePopup by viewModel.showRafflePopup.collectAsState()

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (homeState) {
                HomeState.LOCKED -> {
                    HomeLockedScreen(
                        onGoToAR = {
                            viewModel.onMascotCollected()

                            // navController.navigate("ar_screen")
                        }
                    )
                }

                HomeState.FIRST_ENTER -> {
                    NewFriendPopup(
                        onDismiss = { viewModel.finishFirstEnter() }
                    )
                }

                HomeState.ROOM -> {
                    // (1) 방과 오브제 렌더링
                    MascotRoom(
                        objects = objects,
                        onQuestTest = {
                            viewModel.debugProgressQuest()
                        }
                    )

                    // (2) 퀘스트 진행도 UI (위치 수정됨)
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

            if (showRafflePopup) {
                RaffleTicketPopup(
                    onDismiss = { viewModel.closeRafflePopup() }
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
    // 완료 여부 체크
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
            onClick = {
                onHeaderClick()
            }
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