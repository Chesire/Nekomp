package com.chesire.nekomp.feature.airing.ui

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime

data class UIState(
    val airingSeries: PersistentMap<DayOfWeek, PersistentList<AiringItem>> = persistentMapOf(),
    val viewEvent: ViewEvent? = null
)

data class AiringItem(
    val title: String,
    val airingTime: LocalDateTime,
    val posterImage: String
)
