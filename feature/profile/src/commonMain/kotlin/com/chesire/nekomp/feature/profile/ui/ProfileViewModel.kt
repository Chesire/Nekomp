package com.chesire.nekomp.feature.profile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekomp.core.ext.toBestImage
import com.chesire.nekomp.core.model.EntryStatus
import com.chesire.nekomp.core.model.Type
import com.chesire.nekomp.core.preferences.ApplicationSettings
import com.chesire.nekomp.library.datasource.favorite.Favorite
import com.chesire.nekomp.library.datasource.favorite.FavoriteRepository
import com.chesire.nekomp.library.datasource.library.LibraryRepository
import com.chesire.nekomp.library.datasource.stats.StatsRepository
import com.chesire.nekomp.library.datasource.user.UserRepository
import kotlin.time.Duration.Companion.seconds
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val statsRepository: StatsRepository,
    private val favoriteRepository: FavoriteRepository,
    private val libraryRepository: LibraryRepository,
    private val applicationSettings: ApplicationSettings
) : ViewModel() {

    private val _userData: Flow<UserData>
        get() = userRepository.user.map { user ->
            val imageQuality = applicationSettings.imageQuality.first()
            UserData(
                avatarImage = user.avatar.toBestImage(imageQuality),
                name = user.name,
                about = user.about
            )
        }
    private val _highlightsData: Flow<HighlightsData>
        get() = combine(
            statsRepository.consumedAnime,
            statsRepository.consumedManga
        ) { animeStats, mangaStats ->
            HighlightsData(
                episodesWatched = animeStats.units.toString(),
                chaptersRead = mangaStats.units.toString(),
                timeSpentWatching = animeStats.time.convertToDateString(),
                seriesCompleted = animeStats.completed.toString()
            )
        }
    private val _completedData: Flow<CompletedData>
        get() = libraryRepository.libraryEntries.map { libraryEntries ->
            val anime = libraryEntries.filter { it.type == Type.Anime }
            val manga = libraryEntries.filter { it.type == Type.Manga }
            val completedAnime = anime.count { it.entryStatus == EntryStatus.Completed }
            val completedManga = manga.count { it.entryStatus == EntryStatus.Completed }
            CompletedData(
                animeProgress = "$completedAnime/${anime.count()}",
                animePercent = completedAnime.toFloat() / anime.count().toFloat(),
                mangaProgress = "$completedManga/${manga.count()}",
                mangaPercent = completedManga.toFloat() / manga.count().toFloat()
            )
        }
    private val _favoritesData: Flow<FavoritesData>
        get() = combine(
            favoriteRepository.favoriteCharacters,
            favoriteRepository.favoriteAnime,
            favoriteRepository.favoriteManga
        ) { favCharacters, favAnime, favManga ->
            val imageQuality = applicationSettings.imageQuality.first()
            fun List<Favorite>.uiMap(): ImmutableList<String> {
                return this
                    .sortedBy { it.rank }
                    .map { it.image.toBestImage(imageQuality) }
                    .toPersistentList()
            }
            FavoritesData(
                favoriteCharacters = favCharacters.uiMap(),
                favoriteAnime = favAnime.uiMap(),
                favoriteManga = favManga.uiMap()
            )
        }

    val uiState = combine(
        _userData,
        _highlightsData,
        _completedData,
        _favoritesData,
    ) { userData, highlightsData, backlogData, favoritesData ->
        UIState(
            user = userData,
            highlights = highlightsData,
            backlog = backlogData,
            favorites = favoritesData
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UIState()
    )

    private fun Int.convertToDateString(): String {
        if (this <= 0) {
            return "0 hours"
        }

        val duration = this.seconds
        val parts = mutableListOf<String>()
        duration.toComponents { days, hours, minutes, seconds, _ ->
            if (days > 0) {
                parts.add("$days day${if (days > 1) "s" else ""}")
            }
            if (hours > 0) {
                parts.add("$hours hour${if (hours > 1) "s" else ""}")
            }
            if (parts.isEmpty()) {
                parts.add("0 hours")
            }
        }

        return parts.joinToString(", ")
    }
}
