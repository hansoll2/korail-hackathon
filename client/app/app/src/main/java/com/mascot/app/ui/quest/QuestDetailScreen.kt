package com.mascot.app.ui.quest

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage // ★ Coil 임포트 필수
import com.mascot.app.R
import com.mascot.app.data.model.Quest
import kotlinx.coroutines.delay
import androidx.compose.foundation.background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestDetailScreen(navController: NavController, questId: Int) {
    // 임시 데이터 가져오기 (없으면 에러 방지용 기본값)
    val quest = getDummyQuestById(questId) ?: Quest(0, "로딩 실패", "데이터 없음", "https://via.placeholder.com/150", false)

    var verificationState by remember { mutableStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("미션 정보") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    if (verificationState == 0) verificationState = 1
                    else if (verificationState == 2) navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth().padding(16.dp).height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (verificationState == 2) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary
                ),
                enabled = verificationState != 1
            ) {
                when (verificationState) {
                    0 -> Text("📸 인증샷 찍고 완료하기", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    1 -> Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("AI가 사진 분석 중...", fontSize = 16.sp)
                    }
                    2 -> Text("🎉 인증 완료! (보상 받기)", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { innerPadding ->

        LaunchedEffect(verificationState) {
            if (verificationState == 1) {
                delay(1000)
                delay(1500)
                verificationState = 2
                snackbarHostState.showSnackbar("인증에 성공했습니다! 마스코트 호감도 +50 ❤️")
            }
        }

        Column(
            modifier = Modifier.padding(innerPadding).verticalScroll(rememberScrollState()).padding(24.dp)
        ) {
            // (1) 이미지 영역 - AsyncImage로 교체됨!
            Card(
                modifier = Modifier.fillMaxWidth().height(240.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                AsyncImage(
                    model = quest.imageUrl, // ★ URL 사용
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    placeholder = painterResource(id = R.drawable.mascot_image),
                    error = painterResource(id = R.drawable.mascot_image)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // (2) 뱃지
            Row {
                BadgeText(text = "난이도 ⭐⭐")
                Spacer(modifier = Modifier.width(8.dp))
                BadgeText(text = "보상: 뱃지 1개", color = Color(0xFFFFF3E0), textColor = Color(0xFFE65100))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // (3) 제목 & 설명
            Text(text = quest.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "인증 장소: 현재 위치 주변", color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "미션 가이드", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = quest.description, style = MaterialTheme.typography.bodyLarge, lineHeight = 26.sp, color = Color.DarkGray)
        }
    }
}

@Composable
fun BadgeText(text: String, color: Color = Color(0xFFE3F2FD), textColor: Color = Color(0xFF1976D2)) {
    Box(modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(color).padding(horizontal = 10.dp, vertical = 6.dp)) {
        Text(text = text, style = MaterialTheme.typography.labelMedium, color = textColor, fontWeight = FontWeight.Bold)
    }
}

// ★ 더미 데이터도 URL 버전으로 수정됨
fun getDummyQuestById(id: Int): Quest? {
    val allDummies = listOf(
        Quest(1, "성심당 튀김소보로 구매", "대전의 명물! 갓 튀긴 튀김소보로를 구매하고 인증샷을 찍으세요.", "https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png"),
        Quest(2, "엑스포 다리 건너기", "야경을 배경으로 마스코트와 사진 찍기", "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e3/Expo_Bridge.jpg/640px-Expo_Bridge.jpg"),
        Quest(3, "오월드 사파리 구경", "동물 친구들을 만나보세요!", "https://upload.wikimedia.org/wikipedia/commons/thumb/8/82/Daejeon_Station_20180915.jpg/640px-Daejeon_Station_20180915.jpg"),
        Quest(0, "대전역 가락국수 먹기", "출출하시죠? 대전역의 명물 가락국수 한 그릇 어때요?", "https://upload.wikimedia.org/wikipedia/commons/thumb/8/82/Daejeon_Station_20180915.jpg/640px-Daejeon_Station_20180915.jpg")
    )

    return allDummies.find { it.id == id } ?: allDummies.firstOrNull()
}