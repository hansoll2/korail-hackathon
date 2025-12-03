package com.mascot.app.data.model

data class Quest(
    val id: Int,
    val title: String,       // 퀘스트 제목
    val description: String, // 설명
    val imageUrl: String,
    val isCompleted: Boolean = false // 완료 여부
)