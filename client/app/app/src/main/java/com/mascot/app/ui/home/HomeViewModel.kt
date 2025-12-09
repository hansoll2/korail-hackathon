package com.mascot.app.ui.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {

    private val _state = MutableStateFlow(HomeState.LOCKED)
    val state = _state.asStateFlow()

    private val _objects = MutableStateFlow<List<RoomObject>>(emptyList())
    val objects = _objects.asStateFlow()


    fun onMascotCollected() {
        _state.value = HomeState.FIRST_ENTER
    }

    fun finishFirstEnter() {
        _state.value = HomeState.ROOM
    }

    fun addObject(obj: RoomObject) {
        _objects.value = _objects.value + obj
    }
}
