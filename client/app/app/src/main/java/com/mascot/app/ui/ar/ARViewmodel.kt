package com.mascot.app.ui.ar

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.mascot.app.data.encyclopediadata.dao.MascotDao
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


@HiltViewModel
class ARViewmodel @Inject constructor(
    private val mascotDao: MascotDao
) : ViewModel() {

    fun onMascotCollected(mascotId: Int) {
        viewModelScope.launch {
            mascotDao.markMascotAsCollected(mascotId)
        }
    }
}
