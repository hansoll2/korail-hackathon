package com.mascot.app.data.repository

import com.mascot.app.data.model.Quest
import com.mascot.app.data.remote.QuestApi

class QuestRepository(private val api: QuestApi) {

    suspend fun generateQuests(userInfo: Map<String, Any>): List<Quest> {
        return api.generateQuest(userInfo).quests
    }
}
