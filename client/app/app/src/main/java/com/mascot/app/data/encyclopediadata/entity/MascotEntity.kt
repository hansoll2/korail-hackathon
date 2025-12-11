package com.mascot.app.data.encyclopediadata.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "mascots",
    foreignKeys = [
        ForeignKey(
            entity = ZoneEntity::class,
            parentColumns = ["id"],
            childColumns = ["zoneId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("zoneId")]
)
data class MascotEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val zoneId: Int,               // ✔ zone 기반으로 변경됨
    val name: String,
    val imageUrl: String?,
    val region: String,
    val description: String,
    val isCollected: Boolean = false
)
