
package com.mascot.app.ui.quest

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestDetailScreen(
    navController: NavController,
    viewModel: QuestViewModel = viewModel(),
    questId: Int? = null
) {
    val quests by viewModel.quests.collectAsState()

    // id가 String이라 questId(Int?)를 String으로 변환 후 비교
    val quest = quests.find { it.id == questId?.toString() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "퀘스트 상세") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            /** ---------------------- 퀘스트 정보 ------------------------ **/
            quest?.let {
                Text(
                    text = it.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = it.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            /** ---------------------- 튜토리얼 시작 버튼 ------------------------ **/
            Button(
                onClick = {
                    navController.navigate("tutorial_start")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4A90E2),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "튜토리얼 시작하기",
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
        }
    }
}
