package com.chesire.nekomp.library.datasource.stats

data class ConsumedStats(
    val type: ConsumedStatsType,
    val time: Int,
    val media: Int,
    val units: Int,
    val completed: Int
)
