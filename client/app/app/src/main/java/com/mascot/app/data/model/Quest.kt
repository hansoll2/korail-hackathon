package com.mascot.app.data.model

data class Quest(
    val id: String,
    val title: String,
    val description: String,
    val location: String? = null,
    val duration: String? = null,
    val type: String? = null,
    val imageUrl: String? = null,
    val isCompleted: Boolean = false
)

