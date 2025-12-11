package com.mascot.app.data.encyclopediadata.initial

data class MascotData(
    val zones: List<ZoneJson>
)

data class ZoneJson(
    val id: Int,
    val name: String,       // 도시명: 대전, 세종, 서울
    val region: String,     // 권역명: 충청도, 서울, 수도권
    val mascot: MascotJson
)

data class MascotJson(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val region: String,     // 마스코트 지역(도시명): 대전, 세종 등
    val description: String,
    val isCollected: Boolean
)
