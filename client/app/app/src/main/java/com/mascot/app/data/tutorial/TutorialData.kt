package com.mascot.app.data.tutorial

data class TutorialData(
    val userId: String,
    val name: String,
    val ageRange: String,
    val gender: String,
    val purposes: List<String>,
    val companions: List<String>
)
