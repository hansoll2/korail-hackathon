package com.mascot.app.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    // 뷰모델 상태 구독
    val homeState by viewModel.screenState.collectAsState()
    val objects by viewModel.unlockedObjects.collectAsState()

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (homeState) {
                /** 🔒 1. 마스코트 없음 → 잠금 화면 (상자) */
                HomeState.LOCKED -> {
                    HomeLockedScreen(
                        onGoToAR = {
                            // 실제 AR 화면 이동 코드: navController.navigate("ar")
                            // [테스트용] 클릭 시 바로 마스코트 획득 처리
                            viewModel.onMascotCollected()
                        }
                    )
                }

                /** ✨ 2. 첫 마스코트 획득 → 등장 애니메이션 */
                HomeState.FIRST_ENTER -> {
                    NewFriendPopup(
                        onDismiss = { viewModel.finishFirstEnter() }
                    )
                }

                /** 🏠 3. 마스코트 보유 → 방 꾸미기 화면 */
                HomeState.ROOM -> {
                    // 방과 오브제를 보여줍니다.
                    MascotRoom(
                        objects = objects,
                        onQuestTest = {
                            // [테스트용] 클릭 시 튀김소보로 획득
                            viewModel.completeQuest("튀김소보로")
                        }
                    )
                }
            }
        }
    }
}