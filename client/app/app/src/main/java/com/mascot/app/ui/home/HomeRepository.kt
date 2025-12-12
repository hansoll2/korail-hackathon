package com.mascot.app.ui.home

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor() {

    private val _myObjects = mutableListOf<String>()

    fun getObjects(): List<String> = _myObjects

    fun addNextObject() {
        val nextIndex = _myObjects.size
        val allItems = listOf("튀김소보로", "한빛탑", "대전오월드")

        if (nextIndex < allItems.size) {
            _myObjects.add(allItems[nextIndex])
        }
    }
}