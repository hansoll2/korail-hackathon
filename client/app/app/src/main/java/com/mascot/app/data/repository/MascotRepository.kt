package com.mascot.app.data.repository

import com.mascot.app.data.encyclopediadata.dao.MascotDao
import javax.inject.Inject

// 1. 인터페이스 (메뉴판 역할)
interface MascotRepository {
    suspend fun checkIsCollected(mascotId: Int): Boolean
    suspend fun collectMascot(mascotId: Int)
}

// 2. 구현체 (실제 요리사 역할)
class MascotRepositoryImpl @Inject constructor(
    private val mascotDao: MascotDao
) : MascotRepository {

    // 수집 여부 확인
    override suspend fun checkIsCollected(mascotId: Int): Boolean {
        // DAO에 이 함수가 없으면 3단계에서 추가해야 합니다.
        return mascotDao.isMascotCollected(mascotId)
    }

    // 마스코트 수집 (업데이트)
    override suspend fun collectMascot(mascotId: Int) {
        mascotDao.markMascotAsCollected(mascotId) // 기존에 쓰시던 업데이트 함수 이름
    }
}