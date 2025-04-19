package com.chesire.nekomp.feature.discover.ui

import androidx.compose.runtime.Stable
import com.chesire.nekomp.core.model.Type
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class UIState(
    val searchTerm: String = "",
    val recentSearches: ImmutableList<String> = persistentListOf(),

    val trendingState: TrendingState = TrendingState(),
    val resultsState: ResultsState = ResultsState(),
    val detailState: DetailState = DetailState(),

    val viewEvent: ViewEvent? = null
)

@Stable
data class TrendingState(
    val trendingAnime: ImmutableList<DiscoverItem> = persistentListOf(),
    val trendingManga: ImmutableList<DiscoverItem> = persistentListOf(),
    val topRatedAnime: ImmutableList<DiscoverItem> = persistentListOf(),
    val topRatedManga: ImmutableList<DiscoverItem> = persistentListOf(),
    val mostPopularAnime: ImmutableList<DiscoverItem> = persistentListOf(),
    val mostPopularManga: ImmutableList<DiscoverItem> = persistentListOf()
)

@Stable
data class ResultsState(
    val searchResults: ImmutableList<DiscoverItem> = persistentListOf(),
)

@Stable
data class DetailState(
    val currentItem: DiscoverItem? = null
)

data class DiscoverItem(
    val id: Int,
    val title: String,
    val type: Type,
    val coverImage: String,
    val posterImage: String,
    val isTracked: Boolean = false,
    val isPendingTrack: Boolean = false
)
