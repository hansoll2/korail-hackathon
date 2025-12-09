package com.mascot.app.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val homeState by viewModel.state.collectAsState()
    val objects by viewModel.objects.collectAsState()

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            when (homeState) {

                /** 🔒 마스코트 없음 → 잠금 화면 */
                HomeState.LOCKED -> {
                    HomeLockedScreen(
                        onGoToAR = {
                            navController.navigate("ar")
                        }
                    )
                }

                /** ✨ 첫 마스코트 획득 → 등장 애니메이션 */
                HomeState.FIRST_ENTER -> {
                    MascotMeetAnimation(
                        onFinish = { viewModel.finishFirstEnter() }
                    )
                }

                /** 🏠 마스코트 보유 → 정상 방 화면 */
                HomeState.ROOM -> {
                    MascotRoom(
                        objects = objects
                    )
                }
            }
        }
    }
}
