package com.chesire.nekomp.core.model

import kotlinx.datetime.DayOfWeek

data class AiringTime(
    val dayOfWeek: DayOfWeek,
    val hour: Int,
    val minute: Int,
    val timeZone: String
)
