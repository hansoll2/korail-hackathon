package com.mascot.app.ui.ar

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ARViewmodel @Inject constructor() : ViewModel() {


    fun onMascotCollected(mascotId: Int) {
        // 여기에 코드 추가하면 됨
    }
}