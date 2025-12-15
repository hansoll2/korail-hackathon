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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.mascot.app.R
import com.mascot.app.ui.Screen

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val homeState by viewModel.screenState.collectAsState()
    val objects by viewModel.unlockedObjects.collectAsState()
    val questCount by viewModel.questCount.collectAsState()
    val showRafflePopup by viewModel.showRafflePopup.collectAsState()

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (homeState) {
                // 1. 잠김 상태
                HomeState.LOCKED -> {
                    // HomeLockedScreen이 다른 파일에 있다면 자동 import 됩니다.
                    HomeLockedScreen(
                        onGoToAR = {
                            navController.navigate(Screen.AR.route) {
                                popUpTo(navController.graph.findStartDestination().id)
                                launchSingleTop = true
                            }
                        }
                    )
                }

                // 2. 수집 직후 팝업 상태
                HomeState.FIRST_ENTER -> {
                    // ✨ 이미 다른 파일에 만들어두신 NewFriendPopup을 호출합니다.
                    NewFriendPopup(
                        onDismiss = {
                            viewModel.finishFirstEnter()
                        }
                    )
                }

                // 3. 메인 방 (ROOM)
                HomeState.ROOM -> {
                    MascotRoom(
                        objects = objects,
                        onQuestTest = {
                            viewModel.debugProgressQuest()
                        }
                    )

                    // 퀘스트 진행도 UI
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
                // ✨ 이미 다른 파일에 만들어두신 RaffleTicketPopup을 호출합니다.
                RaffleTicketPopup(
                    onDismiss = { viewModel.closeRafflePopup() }
                )
            }
        }
    }
}

// QuestProgressUI는 HomeScreen 전용이라 여기에 둬도 되지만,
// 만약 다른 파일에도 있다면 이것도 지워야 합니다. (지금은 남겨둠)
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