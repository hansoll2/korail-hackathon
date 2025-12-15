package com.mascot.app.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mascot.app.data.repository.MascotRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MascotRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _screenState = MutableStateFlow(HomeState.LOCKED)
    val screenState: StateFlow<HomeState> = _screenState.asStateFlow()

    private val _unlockedObjects = MutableStateFlow<List<String>>(emptyList())
    val unlockedObjects: StateFlow<List<String>> = _unlockedObjects.asStateFlow()

    private val _questCount = MutableStateFlow(0)
    val questCount: StateFlow<Int> = _questCount.asStateFlow()

    private val _showRafflePopup = MutableStateFlow(false)
    val showRafflePopup: StateFlow<Boolean> = _showRafflePopup.asStateFlow()

    init {
        checkInitialState()
    }

    private fun checkInitialState() {
        viewModelScope.launch {
            // 1. 방금 AR에서 잡고 돌아왔는지 확인
            val isJustCollected = savedStateHandle.get<Boolean>("new_mascot") ?: false

            if (isJustCollected) {
                // ✨ [핵심] 바로 방으로 안 가고, '팝업(FIRST_ENTER)'을 먼저 보여줍니다!
                _screenState.value = HomeState.FIRST_ENTER

                // 신호 소비
                savedStateHandle["new_mascot"] = false
            } else {
                // 2. 그게 아니면 DB 확인 (앱 껐다 켰을 때)
                val isCollected = repository.checkIsCollected(1001)

                if (isCollected) {
                    // 예전에 잡은 적 있으면 -> 팝업 없이 바로 방(ROOM)
                    _screenState.value = HomeState.ROOM
                } else {
                    // 없으면 -> 잠김(LOCKED)
                    _screenState.value = HomeState.LOCKED
                }
            }
        }
    }

    // ✨ [핵심] 팝업에서 [확인] 버튼을 눌렀을 때 호출되는 함수
    fun finishFirstEnter() {
        // 이제 팝업을 닫고 -> 방(ROOM)으로 이동
        _screenState.value = HomeState.ROOM
    }

    // ... (나머지 퀘스트/래플 관련 로직 유지)
    fun debugProgressQuest() {
        val currentCount = _questCount.value
        if (currentCount < 3) {
            _questCount.value = currentCount + 1
            val newObject = when (currentCount) {
                0 -> "튀김소보로"
                1 -> "한빛탑"
                2 -> "대전오월드"
                else -> ""
            }
            if (newObject.isNotEmpty() && !_unlockedObjects.value.contains(newObject)) {
                _unlockedObjects.value = _unlockedObjects.value + newObject
            }
        }
    }

    fun openRafflePopup() { if (_questCount.value >= 3) _showRafflePopup.value = true }
    fun closeRafflePopup() { _showRafflePopup.value = false; _questCount.value = 0 }
}