package com.chesire.nekomp.feature.discover.ui

import com.chesire.nekomp.core.model.Type
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class UIState(
    val searchTerm: String = "",
    val trendingAnime: ImmutableList<DiscoverItem> = persistentListOf(),
    val trendingManga: ImmutableList<DiscoverItem> = persistentListOf(),
    val topRatedAnime: ImmutableList<DiscoverItem> = persistentListOf(),
    val topRatedManga: ImmutableList<DiscoverItem> = persistentListOf(),
    val mostPopularAnime: ImmutableList<DiscoverItem> = persistentListOf(),
    val mostPopularManga: ImmutableList<DiscoverItem> = persistentListOf(),
    val viewEvent: ViewEvent? = null
)

data class DiscoverItem(
    val id: Int,
    val title: String,
    val type: Type,
    val coverImage: String,
    val isTracked: Boolean = false,
    val isPendingTrack: Boolean = false
)
