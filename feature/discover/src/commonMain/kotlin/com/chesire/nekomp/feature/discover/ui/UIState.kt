package com.chesire.nekomp.feature.discover.ui

import com.chesire.nekomp.core.model.Type
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

// TODO: Maybe split this into LIST and DETAIL classes?
// TODO: Or Trending, Search, and Results classes?
data class UIState(
    val searchTerm: String = "",
    val recentSearches: ImmutableList<String> = persistentListOf(),
    val trendingAnime: ImmutableList<DiscoverItem> = persistentListOf(),
    val trendingManga: ImmutableList<DiscoverItem> = persistentListOf(),
    val topRatedAnime: ImmutableList<DiscoverItem> = persistentListOf(),
    val topRatedManga: ImmutableList<DiscoverItem> = persistentListOf(),
    val mostPopularAnime: ImmutableList<DiscoverItem> = persistentListOf(),
    val mostPopularManga: ImmutableList<DiscoverItem> = persistentListOf(),
    val searchResults: ImmutableList<SearchItem> = persistentListOf(),
    val viewEvent: ViewEvent? = null
)

// TODO: Slim these down to a single item
data class DiscoverItem(
    val id: Int,
    val title: String,
    val type: Type,
    val coverImage: String,
    val isTracked: Boolean = false,
    val isPendingTrack: Boolean = false
)

data class SearchItem(
    val id: Int,
    val title: String,
    val type: Type,
    val posterImage: String,
    val isTracked: Boolean = false
)
