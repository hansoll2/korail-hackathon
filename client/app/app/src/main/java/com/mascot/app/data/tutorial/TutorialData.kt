package com.mascot.app.data.tutorial

data class TutorialData(
    val name: String,               // 이름
    val ageRange: String,           // 연령대 (10대, 20대...)
    val gender: String,             // 성별

    val purposes: List<String>,     // 여행 목적 (여러 개 선택)
    val companions: List<String>,   // 동행 정보 (여러 개 선택)

    val customPurpose: String?,     // 기타 목적 텍스트 (없으면 null)
    val customCompanion: String?    // 기타 동행 텍스트 (없으면 null)
)