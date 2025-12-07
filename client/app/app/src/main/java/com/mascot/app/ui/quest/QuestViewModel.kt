package com.mascot.app.ui.quest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mascot.app.data.model.Quest
import com.mascot.app.data.remote.RetrofitModule
import com.mascot.app.data.repository.QuestRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuestViewModel : ViewModel() {

    private val repository = QuestRepository(RetrofitModule.api)

    private val _quests = MutableStateFlow<List<Quest>>(emptyList())
    val quests = _quests.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    fun requestQuests(theme: String) {
        println(">>> requestQuests() 실행됨, theme=$theme")
        viewModelScope.launch {
            println(">>> Coroutine 시작됨")
            _loading.value = true

            val userInfo = mapOf(
                "theme" to theme,
                "location" to "대전역",
                "age" to 24
            )

            try {
                val list = repository.generateQuests(userInfo)
                _quests.value = list + _quests.value // 최신 퀘스트를 위로 추가
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _loading.value = false
            }
        }
    }
}
