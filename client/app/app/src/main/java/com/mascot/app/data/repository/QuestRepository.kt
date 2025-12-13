package com.mascot.app.data.repository

import com.mascot.app.data.model.QuestItem
import com.mascot.app.data.remote.RetrofitModule

class QuestRepository {

    private val api = RetrofitModule.questApi

    /**
     * 전체(구별 Map) 퀘스트 가져오기
     * - QuestViewModel 에서 repository.getAllQuests() 로 호출하는 함수
     */
    suspend fun getAllQuests(userId: String): Map<String, List<QuestItem>> {
        val regions = listOf("중구", "서구", "유성구", "대덕구", "동구")
        val result = mutableMapOf<String, List<QuestItem>>()

        for (region in regions) {
            try {
                val response = api.getQuestsByRegion(region, userId)
                result[region] = response.quests
            } catch (e: Exception) {
                result[region] = emptyList()
            }
        }
        return result
    }


}
