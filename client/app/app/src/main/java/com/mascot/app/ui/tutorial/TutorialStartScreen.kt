package com.mascot.app.ui.tutorial

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mascot.app.R
import com.mascot.app.data.tutorial.TutorialData
import com.mascot.app.data.remote.RetrofitModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.util.Log

@Composable
fun TutorialStartScreen(navController: NavController) {

    var step by remember { mutableStateOf(1) }

    var name by remember { mutableStateOf("") }
    var ageRange by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }

    val purposeList = remember { mutableStateListOf<String>() }
    val companionList = remember { mutableStateListOf<String>() }

    // 🔥 임시 userId
    val userId = "test-user"

    // 🔥 퀘스트 생성중 상태
    var isGenerating by remember { mutableStateOf(false) }

    val bubbleText = when (step) {
        1 -> "안녕 난 꿈돌이야 대전에 온 걸 환영해!\n맞춤 퀘스트 만들기 튜토리얼을 시작할게"
        2 -> "먼저 너의 이름을 알려줄래?"
        3 -> "연령대를 선택해줘"
        4 -> "성별을 선택해줘"
        5 -> "여행 목적이 뭐야? (다중 선택 가능)"
        6 -> "함께 여행하는 사람은? (다중 선택 가능)"
        else -> ""
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F3F3))
    ) {

        /* ================== 튜토리얼 UI ================== */
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(20.dp)
            ) {
                Text(text = bubbleText, fontSize = 18.sp, textAlign = TextAlign.Center)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Image(
                painter = painterResource(id = R.drawable.char_kumdori),
                contentDescription = "Mascot",
                modifier = Modifier.size(300.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            /* ---------- Step 1 ---------- */
            if (step == 1) {
                Button(
                    onClick = { step = 2 },
                    modifier = Modifier.width(220.dp).height(50.dp)
                ) { Text("좋아!", color = Color.White) }
            }

            /* ---------- Step 2 ---------- */
            if (step == 2) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("이름 입력") },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 40.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { step = 3 },
                    enabled = name.isNotEmpty()
                ) { Text("다음") }
            }

            /* ---------- Step 3 ---------- */
            if (step == 3) {
                listOf("10대 미만", "10대", "20대", "30대", "40대", "50대 이상")
                    .chunked(2).forEach { row ->
                        Row(horizontalArrangement = Arrangement.Center) {
                            row.forEach {
                                SelectButtonBlue(it, ageRange == it) {
                                    ageRange = it
                                    step = 4
                                }
                            }
                        }
                    }
            }

            /* ---------- Step 4 ---------- */
            if (step == 4) {
                listOf("남성", "여성", "선택 안 함")
                    .chunked(2).forEach { row ->
                        Row(horizontalArrangement = Arrangement.Center) {
                            row.forEach {
                                SelectButtonBlue(it, gender == it) {
                                    gender = it
                                    step = 5
                                }
                            }
                        }
                    }
            }

            /* ---------- Step 5 ---------- */
            if (step == 5) {
                listOf("관광", "휴식", "사진", "맛집", "체험", "기타")
                    .chunked(3).forEach { row ->
                        Row(horizontalArrangement = Arrangement.Center) {
                            row.forEach {
                                ToggleButtonSmall(it, purposeList.contains(it)) {
                                    if (purposeList.contains(it)) purposeList.remove(it)
                                    else purposeList.add(it)
                                }
                            }
                        }
                    }

                Button(
                    onClick = { step = 6 },
                    enabled = purposeList.isNotEmpty()
                ) { Text("다음") }
            }

            /* ---------- Step 6 ---------- */
            if (step == 6) {
                listOf("혼자", "친구", "가족", "연인", "단체", "기타")
                    .chunked(3).forEach { row ->
                        Row(horizontalArrangement = Arrangement.Center) {
                            row.forEach {
                                ToggleButtonSmall(it, companionList.contains(it)) {
                                    if (companionList.contains(it)) companionList.remove(it)
                                    else companionList.add(it)
                                }
                            }
                        }
                    }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    enabled = companionList.isNotEmpty() && !isGenerating,
                    onClick = {
                        val tutorialData = TutorialData(
                            userId = userId,
                            name = name,
                            ageRange = ageRange,
                            gender = gender,
                            purposes = purposeList.toList(),
                            companions = companionList.toList()
                        )

                        isGenerating = true

                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                RetrofitModule.questApi.generateQuestAll(tutorialData)

                                withContext(Dispatchers.Main) {
                                    navController.navigate("quest") {
                                        popUpTo("tutorial_start") { inclusive = true }
                                    }
                                }
                            } catch (e: Exception) {
                                Log.e("TUTORIAL", "서버 요청 실패: ${e.message}")
                                withContext(Dispatchers.Main) {
                                    isGenerating = false
                                }
                            }
                        }
                    }
                ) {
                    Text("완료!", color = Color.White)
                }
            }
        }

        /* ================== 🔥 생성중 오버레이 (맨 위) ================== */
        if (isGenerating) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = Color.White)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "퀘스트 생성중...",
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

/* ---------- 버튼 컴포넌트 ---------- */

@Composable
fun SelectButtonBlue(text: String, selected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.width(140.dp).height(50.dp).padding(6.dp)
    ) { Text(text, color = Color.White) }
}

@Composable
fun ToggleButtonSmall(text: String, selected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.width(110.dp).height(45.dp).padding(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primary else Color.Gray
        )
    ) { Text(text, color = Color.White) }
}
