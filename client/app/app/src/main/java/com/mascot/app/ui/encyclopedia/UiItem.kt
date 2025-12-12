package com.mascot.app.ui.encyclopedia


sealed class UiItem {

    // 권역명
    data class Header(
        val title: String
    ) : UiItem()

    // 한 줄(Row)에 여러 마스코트를 넣는 구조
    data class MascotRow(
        val mascots: List<MascotItem>
    ) : UiItem()

    data class MascotItem(
        val id: Int,
        val title: String,
        val imageUrl: String?,
        val isCollected: Boolean
    ) : UiItem()
}
