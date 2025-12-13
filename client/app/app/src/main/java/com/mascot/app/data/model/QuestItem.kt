package com.mascot.app.data.model

data class QuestItem(
    val id: String,
    val title: String,
    val description: String,
    val location: String,
    val category: String?,
    val coordinates: Coordinates
)

data class Coordinates(
    val lat: Double,
    val lng: Double
)
