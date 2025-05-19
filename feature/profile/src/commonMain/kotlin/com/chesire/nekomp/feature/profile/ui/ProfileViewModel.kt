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
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

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
                episodesWatched = animeStats.units,
                chaptersRead = mangaStats.units,
                timeSpentWatching = animeStats.time.toString(), // TODO: Convert to proper string
                seriesCompleted = animeStats.completed
            )
        }
    private val _backlogData: Flow<BacklogData>
        get() = libraryRepository.libraryEntries.map { libraryEntries ->
            val anime = libraryEntries.filter { it.type == Type.Anime }
            val manga = libraryEntries.filter { it.type == Type.Manga }
            val plannedAnime = anime.count { it.entryStatus == EntryStatus.Planned }
            val plannedManga = manga.count { it.entryStatus == EntryStatus.Planned }
            BacklogData(
                animeProgress = "$plannedAnime/${anime.count() - plannedAnime}",
                animePercent = anime.count().toFloat() / plannedAnime.toFloat(),
                mangaProgress = "$plannedManga/${manga.count() - plannedManga}",
                mangaPercent = manga.count()
                    .toFloat() / plannedManga.toFloat() // TODO: These probably aren't right
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
    private val _viewEvent = MutableStateFlow<ViewEvent?>(null)

    // TODO: Check if this combine takes awhile to run or not, hopefully it doesn't stall the UI
    val uiState = combine(
        _userData,
        _highlightsData,
        _backlogData,
        _favoritesData,
        _viewEvent
    ) { userData, highlightsData, backlogData, favoritesData, viewEvent ->
        UIState(
            user = userData,
            highlights = highlightsData,
            backlog = backlogData,
            favorites = favoritesData,
            viewEvent = viewEvent
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UIState()
    )

    fun execute(action: ViewAction) {
        when (action) {
            ViewAction.ObservedViewEvent -> onObservedViewEvent()
        }
    }

    private fun onObservedViewEvent() {
        _viewEvent.update { null }
    }
}
