package com.chesire.nekomp.feature.home.ui

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class UIState(
    val username: String = "",
    val watchList: ImmutableList<WatchItem> = persistentListOf(),
    val trendingAll: ImmutableList<TrendItem> = persistentListOf(),
    val trendingAnime: ImmutableList<TrendItem> = persistentListOf(),
    val trendingManga: ImmutableList<TrendItem> = persistentListOf(),
    val viewEvent: ViewEvent? = null
)

@Stable
data class WatchItem(
    val entryId: Int,
    val title: String,
    val posterImage: String,
    val progressPercent: Float,
    val progress: Int
)

@Stable
data class TrendItem(
    val id: Int,
    val title: String,
    val posterImage: String
)
