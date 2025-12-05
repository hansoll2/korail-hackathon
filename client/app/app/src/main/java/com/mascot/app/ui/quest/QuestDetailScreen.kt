package com.mascot.app.ui.quest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mascot.app.data.model.Quest
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestDetailScreen(
    navController: NavController,
    questId: String,
    viewModel: QuestViewModel = viewModel()  // ← ViewModel 접근
) {
    // 🔥 ViewModel에서 실제 퀘스트 목록 가져오기
    val allQuests by viewModel.quests.collectAsState()

    // 🔥 id로 퀘스트 찾기
    val quest = allQuests.find { it.id == questId } ?: Quest(
        id = "0",
        title = "퀘스트 정보를 찾을 수 없습니다.",
        description = "AI가 생성한 퀘스트에서 해당 ID가 존재하지 않습니다.",
        imageUrl = "",
        isCompleted = false
    )

    var verificationState by remember { mutableStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("미션 정보") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        bottomBar = {
            BottomButton(navController, verificationState) { newState ->
                verificationState = newState
            }
        }
    ) { innerPadding ->

        // 인증 진행
        LaunchedEffect(verificationState) {
            if (verificationState == 1) {
                delay(1500)
                verificationState = 2
                snackbarHostState.showSnackbar("인증 성공! 보상 지급 🎉")
            }
        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {

            Spacer(modifier = Modifier.height(10.dp))

            Row {
                BadgeText("난이도 ⭐⭐")
                Spacer(modifier = Modifier.width(8.dp))
                BadgeText(
                    "보상: 뱃지 1개",
                    color = Color(0xFFFFF3E0),
                    textColor = Color(0xFFE65100)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(quest.title, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("인증 장소: 현재 위치 주변", color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(24.dp))

            Text("미션 가이드", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = quest.description,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 26.sp,
                color = Color.DarkGray
            )
        }
    }
}

@Composable
fun BottomButton(
    navController: NavController,
    verificationState: Int,
    onStateChange: (Int) -> Unit
) {
    Button(
        onClick = {
            when (verificationState) {
                0 -> onStateChange(1)
                2 -> navController.popBackStack()
            }
        },
        modifier = Modifier.fillMaxWidth().padding(16.dp).height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (verificationState == 2) Color(0xFF4CAF50)
            else MaterialTheme.colorScheme.primary
        ),
        enabled = verificationState != 1
    ) {
        when (verificationState) {
            0 -> Text("📸 인증샷 찍고 완료하기", fontSize = 18.sp)
            1 -> Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                Spacer(modifier = Modifier.width(12.dp))
                Text("AI가 분석 중...", fontSize = 16.sp)
            }
            2 -> Text("🎉 인증 완료! (보상 받기)", fontSize = 18.sp)
        }
    }
}

@Composable
fun BadgeText(text: String, color: Color = Color(0xFFE3F2FD), textColor: Color = Color(0xFF1976D2)) {
    Box(
        modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(color).padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(text = text, color = textColor)
    }
}
