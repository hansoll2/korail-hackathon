package com.mascot.app.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mascot.app.R

@Composable
fun NewFriendPopup(
    onDismiss: () -> Unit
) {
    // DialogProperties를 사용하여 시스템 기본 배경(Dim)을 제거하고 전체 화면을 씁니다.
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false, // 화면 너비 제한 해제
            decorFitsSystemWindows = false   // 시스템 바 영역까지 확장
        )
    ) {
        // 1. 우리가 직접 만드는 반투명 배경
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.2f)) // 0.0f(투명) ~ 1.0f(완전검정) 조절 가능
                .clickable { onDismiss() }, // 배경을 누르면 닫힘 (원치 않으면 이 줄 삭제)
            contentAlignment = Alignment.Center
        ) {
            // 2. 실제 팝업 카드 (배경 클릭 이벤트가 전달되지 않도록 clickable(enabled=false) 추가)
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(10.dp),
                modifier = Modifier
                    .padding(horizontal = 40.dp) // 좌우 여백
                    .clickable(enabled = false) {} // 카드 영역 클릭 시 닫히지 않게 방지
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 타이틀
                    Text(
                        text = "새로운 친구 등장!",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Image(
                        painter = painterResource(id = R.drawable.char_kumdori),

                        contentDescription = "꿈돌이",
                        modifier = Modifier.size(140.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // 메인 텍스트
                    Text(
                        text = "대전의 마스코트\n꿈돌이",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "꿈돌이가 친구가 되었어요!\n이제 함께 여행해요.",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // 버튼
                    Button(
                        onClick = { onDismiss() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("반가워!")
                    }
                }
            }
        }
    }
}