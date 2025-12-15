package com.mascot.app.data.encyclopediadata.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mascot.app.data.encyclopediadata.entity.MascotEntity
import com.mascot.app.data.encyclopediadata.entity.ZoneEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MascotDao {

    // ⭐ Zone 저장
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertZone(zone: ZoneEntity)

    // ⭐ Mascot 저장
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMascot(mascot: MascotEntity)

    // ⭐ 전체 Zone 가져오기
    @Query("SELECT * FROM zones")
    suspend fun getZones(): List<ZoneEntity>

    // ⭐ 특정 Zone 의 Mascot 1개 가져오기
    @Query("SELECT * FROM mascots WHERE zoneId = :zoneId LIMIT 1")
    suspend fun getMascotByZone(zoneId: Int): MascotEntity?

    // ⭐ 전체 마스코트 개수 (도감 진행도 표시용)
    @Query("SELECT COUNT(*) FROM mascots")
    suspend fun getTotalMascotCount(): Int

    // ⭐ 수집된 마스코트 개수
    @Query("SELECT COUNT(*) FROM mascots WHERE isCollected = 1")
    suspend fun getCollectedMascotCount(): Int

    @Query("SELECT * FROM mascots WHERE id = :id LIMIT 1")
    suspend fun getMascotById(id: Int): MascotEntity?

    // 수집 처리 (isCollected = true 로 변경)
    @Query("UPDATE mascots SET isCollected = 1 WHERE id = :mascotId")
    suspend fun markMascotAsCollected(mascotId: Int)

    // ✨✨ [필수 추가] 수집 여부 확인용 쿼리 ✨✨
    // ID에 해당하는 마스코트의 수집 여부(Boolean)만 가져옵니다.
    @Query("SELECT isCollected FROM mascots WHERE id = :id")
    suspend fun isMascotCollected(id: Int): Boolean

    @Query("SELECT * FROM zones")
    fun observeZones(): Flow<List<ZoneEntity>>

    @Query("SELECT * FROM mascots")
    fun observeMascots(): Flow<List<MascotEntity>>
}