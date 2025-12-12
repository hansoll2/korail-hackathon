package com.mascot.app.data.encyclopediadata.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "zones")
data class ZoneEntity(
    @PrimaryKey val id: Int,
    val name: String,       // 도시명: 대전, 세종, 서울
    val region: String      // 권역명: 충청도, 수도권, 강원도 등
)
