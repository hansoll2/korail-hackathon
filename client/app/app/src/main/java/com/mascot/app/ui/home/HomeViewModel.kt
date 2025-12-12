package com.mascot.app.ui.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository // ⭐ 1. 레포지토리 주입 (창고 연결)
) : ViewModel() {

    // 화면 상태 (LOCKED = 박스, FIRST_ENTER = 팝업, ROOM = 방)
    private val _screenState = MutableStateFlow(HomeState.LOCKED)
    val screenState = _screenState.asStateFlow()

    private val _unlockedObjects = MutableStateFlow<List<String>>(emptyList())
    val unlockedObjects = _unlockedObjects.asStateFlow()
    
    init {
        refreshData()
    }

    // 데이터 새로고침 (레포지토리에서 최신 목록 가져오기)
    private fun refreshData() {
        // toList()를 써야 새로운 리스트로 인식해서 화면이 갱신됩니다.
        _unlockedObjects.value = repository.getObjects().toList()
    }

    // [Step 1 -> 2] AR 수집 완료 시 호출 (외부에서 호출)
    fun onMascotCollected() {
        _screenState.value = HomeState.FIRST_ENTER
    }

    // [Step 2 -> 3] 팝업 닫기 (반가워 버튼)
    fun finishFirstEnter() {
        _screenState.value = HomeState.ROOM
    }

    // ⭐ [Quest] 퀘스트 텍스트 클릭 시 실행 (가짜 인증 시뮬레이션)
    fun debugProgressQuest() {
        repository.addNextObject() // 창고에 아이템 추가
        refreshData() // 화면에 반영
    }
}