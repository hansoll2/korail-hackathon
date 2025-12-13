package com.mascot.app.data.remote

import com.mascot.app.data.model.QuestResponse
import com.mascot.app.data.tutorial.TutorialData
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface QuestApi {

    // 🔥 튜토리얼 → 전체 퀘스트 생성
    @POST("generateQuestAll")
    suspend fun generateQuestAll(
        @Body tutorialData: TutorialData
    )

    // 🔥 특정 구 퀘스트 조회 (Query 방식)
    @GET("quests")
    suspend fun getQuestsByRegion(
        @Query("region") region: String,
        @Query("userId") userId: String
    ): QuestResponse
}
