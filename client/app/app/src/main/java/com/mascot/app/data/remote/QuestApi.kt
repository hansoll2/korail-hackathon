package com.mascot.app.data.remote

import com.mascot.app.data.model.QuestResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface QuestApi {

    @POST("generateQuest")
    suspend fun generateQuest(@Body userInfo: Map<String, Any>): QuestResponse
}
