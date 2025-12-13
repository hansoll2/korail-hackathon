package com.mascot.app.ui.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

) : ViewModel() {

    private val _screenState = MutableStateFlow(HomeState.LOCKED)
    val screenState: StateFlow<HomeState> = _screenState.asStateFlow()

    private val _unlockedObjects = MutableStateFlow<List<String>>(emptyList())
    val unlockedObjects: StateFlow<List<String>> = _unlockedObjects.asStateFlow()

    private val _questCount = MutableStateFlow(0)
    val questCount: StateFlow<Int> = _questCount.asStateFlow()


    private val _showRafflePopup = MutableStateFlow(false)
    val showRafflePopup: StateFlow<Boolean> = _showRafflePopup.asStateFlow()


    fun onMascotCollected() {
        _screenState.value = HomeState.FIRST_ENTER
    }

    fun finishFirstEnter() {
        _screenState.value = HomeState.ROOM
    }

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

    fun openRafflePopup() {
        if (_questCount.value >= 3) {
            _showRafflePopup.value = true
        }
    }

    fun closeRafflePopup() {
        _showRafflePopup.value = false
        _questCount.value = 0
    }
}