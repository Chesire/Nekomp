package com.chesire.nekomp.feature.profile.ui

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class UIState(
    val user: UserData = UserData(),
    val highlights: HighlightsData = HighlightsData(),
    val backlog: CompletedData = CompletedData(),
    val favorites: FavoritesData = FavoritesData(),
    val viewEvent: ViewEvent? = null
)

@Immutable
data class UserData(
    val avatarImage: String = "",
    val name: String = "",
    val about: String = ""
)

@Immutable
data class HighlightsData(
    val episodesWatched: String = "",
    val chaptersRead: String = "",
    val timeSpentWatching: String = "",
    val seriesCompleted: String = ""
)

@Immutable
data class CompletedData(
    val animeProgress: String = "",
    val animePercent: Float = 0f,
    val mangaProgress: String = "",
    val mangaPercent: Float = 0f,
)

@Immutable
data class FavoritesData(
    val favoriteCharacters: ImmutableList<String> = persistentListOf(),
    val favoriteAnime: ImmutableList<String> = persistentListOf(),
    val favoriteManga: ImmutableList<String> = persistentListOf()
)
