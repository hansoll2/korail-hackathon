package com.mascot.app.ui.tutorial

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import android.util.Log



@Composable
fun TutorialStartScreen(navController: NavController) {

    var step by remember { mutableStateOf(1) }

    var name by remember { mutableStateOf("") }
    var ageRange by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }

    val purposeList = remember { mutableStateListOf<String>() }
    var purposeCustom by remember { mutableStateOf("") }

    val companionList = remember { mutableStateListOf<String>() }
    var companionCustom by remember { mutableStateOf("") }

    val bubbleText = when (step) {
        1 -> "안녕 난 꿈돌이야 대전에 온 걸 환영해!\n맞춤 퀘스트 만들기 튜토리얼을 시작할게"
        2 -> "먼저 너의 이름을 알려줄래?"
        3 -> "연령대를 선택해줘"
        4 -> "성별을 선택해줘"
        5 -> "여행 목적이 뭐야? (다중 선택 가능)"
        6 -> "누구와 함께 여행 중이야? (다중 선택 가능)"
        else -> ""
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F3F3))
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ------------------------------ 말풍선 ------------------------------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(20.dp)
            ) {
                Text(text = bubbleText, fontSize = 18.sp, textAlign = TextAlign.Center)
            }

            // *** 이미지 위치 올림 ***
            Spacer(modifier = Modifier.height(20.dp))

            Image(
                painter = painterResource(id = R.drawable.mascot_image),
                contentDescription = "Mascot",
                modifier = Modifier.size(300.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // ------------------------------ STEP 1 ------------------------------
            if (step == 1) {
                Button(
                    onClick = { step = 2 },
                    modifier = Modifier
                        .height(50.dp)
                        .width(220.dp)
                ) { Text("좋아!", color = Color.White) }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .height(50.dp)
                        .width(220.dp),
                    colors = ButtonDefaults.buttonColors(Color.Gray)
                ) { Text("잠시만...", color = Color.White) }
            }

            // ------------------------------ STEP 2 — 이름 입력 ------------------------------
            if (step == 2) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("이름 입력") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp)
                )

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = { step = 3 },
                    enabled = name.isNotEmpty(),
                    modifier = Modifier
                        .height(50.dp)
                        .width(200.dp)
                ) { Text("다음", color = Color.White) }
            }

            // ------------------------------ STEP 3 — 연령대 ------------------------------
            if (step == 3) {
                val ages = listOf("10대 미만", "10대", "20대", "30대", "40대", "50대 이상")

                ages.chunked(2).forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        row.forEach { option ->
                            SelectButtonBlue(
                                text = option,
                                selected = ageRange == option
                            ) {
                                ageRange = option
                                step = 4
                            }
                        }
                    }
                }
            }

            // ------------------------------ STEP 4 — 성별 ------------------------------
            if (step == 4) {

                val genders = listOf("남성", "여성", "선택하지 않음")

                genders.chunked(2).forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        row.forEach { option ->
                            SelectButtonBlue(
                                text = option,
                                selected = gender == option
                            ) {
                                gender = option
                                step = 5
                            }
                        }
                    }
                }
            }

            // ------------------------------ STEP 5 — 여행 목적 ------------------------------
            if (step == 5) {

                val purposes = listOf("관광", "휴식", "사진", "맛집", "체험", "기타")

                purposes.chunked(3).forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        row.forEach { option ->
                            ToggleButtonSmall(
                                text = option,
                                selected = purposeList.contains(option)
                            ) {
                                if (purposeList.contains(option)) purposeList.remove(option)
                                else purposeList.add(option)
                            }
                        }
                    }
                }

                if (purposeList.contains("기타")) {
                    OutlinedTextField(
                        value = purposeCustom,
                        onValueChange = { purposeCustom = it },
                        label = { Text("기타 입력") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // *** 버튼 아래 공간 확보 ***
                Button(
                    onClick = { step = 6 },
                    enabled = purposeList.isNotEmpty(),
                    modifier = Modifier
                        .width(200.dp)
                        .padding(bottom = 40.dp)
                ) { Text("다음", color = Color.White) }
            }

            // ------------------------------ STEP 6 — 누구와 함께 ------------------------------
            if (step == 6) {

                val companions = listOf("혼자", "친구", "가족", "연인", "단체", "기타")

                companions.chunked(3).forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        row.forEach { option ->
                            ToggleButtonSmall(
                                text = option,
                                selected = companionList.contains(option)
                            ) {
                                if (companionList.contains(option)) companionList.remove(option)
                                else companionList.add(option)
                            }
                        }
                    }
                }

                if (companionList.contains("기타")) {
                    OutlinedTextField(
                        value = companionCustom,
                        onValueChange = { companionCustom = it },
                        label = { Text("기타 입력") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        // 1) 튜토리얼 결과를 한 객체로 묶기
                        val tutorialData = TutorialData(
                            name = name,
                            ageRange = ageRange,
                            gender = gender,
                            purposes = purposeList.toList(),
                            companions = companionList.toList(),
                            customPurpose = purposeCustom.ifBlank { null },
                            customCompanion = companionCustom.ifBlank { null }
                        )

                        // 2) 지금은 일단 로그로 확인 (다음 단계에서 서버로 보낼 거임)
                        Log.d("Tutorial", "tutorialData = $tutorialData")

                        // 3) 원래 하던 대로 퀘스트 화면으로 이동
                        navController.navigate("quest")
                    },
                    enabled = companionList.isNotEmpty(),
                    modifier = Modifier
                        .width(200.dp)
                        .padding(bottom = 40.dp)
                ) { Text("완료!", color = Color.White) }

            }
        }
    }
}

/* ------------------ 단일 선택 버튼 ------------------ */
@Composable
fun SelectButtonBlue(text: String, selected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(140.dp)
            .height(50.dp)
            .padding(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(12.dp)
    ) { Text(text, color = Color.White) }
}

/* ------------------ 다중 선택 버튼 ------------------ */
@Composable
fun ToggleButtonSmall(text: String, selected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(110.dp)
            .height(45.dp)
            .padding(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor =
                if (selected) MaterialTheme.colorScheme.primary else Color(0xFFBDBDBD)
        ),
        shape = RoundedCornerShape(10.dp)
    ) { Text(text, color = Color.White) }
}