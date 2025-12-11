package com.mascot.app.ui.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    // 1. 화면 상태 (초기값: 잠김)
    private val _screenState = MutableStateFlow(HomeState.LOCKED)
    val screenState = _screenState.asStateFlow()

    // 2. 획득한 오브제 리스트 (이름으로 관리)
    private val _unlockedObjects = MutableStateFlow<List<String>>(emptyList())
    val unlockedObjects = _unlockedObjects.asStateFlow()

    // [Step 1 -> 2] AR 수집 완료 시 호출 (외부에서 호출)
    fun onMascotCollected() {
        _screenState.value = HomeState.FIRST_ENTER
    }

    // [Step 2 -> 3] 애니메이션 종료 시 호출
    fun finishFirstEnter() {
        _screenState.value = HomeState.ROOM
    }

    // [Quest] 퀘스트 완료 및 오브제 추가 (테스트용)
    fun completeQuest(objectName: String) {
        if (!_unlockedObjects.value.contains(objectName)) {
            _unlockedObjects.update { it + objectName }
        }
    }
}