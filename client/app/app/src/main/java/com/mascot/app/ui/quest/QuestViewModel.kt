package com.mascot.app.ui.quest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mascot.app.data.model.QuestItem
import com.mascot.app.data.repository.QuestRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QuestViewModel(
    private val repository: QuestRepository = QuestRepository()
) : ViewModel() {

    private val _quests =
        MutableStateFlow<Map<String, List<QuestItem>>>(emptyMap())
    val quests: StateFlow<Map<String, List<QuestItem>>> = _quests

    private val _completedQuests =
        MutableStateFlow<List<QuestItem>>(emptyList())
    val completedQuests: StateFlow<List<QuestItem>> = _completedQuests

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val userId = "test-user"

    init {
        loadAllQuests()
    }

    private fun loadAllQuests() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _quests.value = repository.getAllQuests(userId)
            } finally {
                _loading.value = false
            }
        }
    }

    /** ✅ 이게 없어서 전부 터졌음 */
    fun completeQuest(quest: QuestItem) {
        _completedQuests.value = _completedQuests.value + quest
    }

}