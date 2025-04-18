package com.chesire.nekomp.feature.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekomp.core.ext.toBestImage
import com.chesire.nekomp.core.ext.toChosenLanguage
import com.chesire.nekomp.core.model.EntryStatus
import com.chesire.nekomp.core.model.Type
import com.chesire.nekomp.core.preferences.ApplicationSettings
import com.chesire.nekomp.core.preferences.ImageQuality
import com.chesire.nekomp.core.preferences.TitleLanguage
import com.chesire.nekomp.feature.home.core.ShowAiringSeriesUseCase
import com.chesire.nekomp.library.datasource.library.LibraryEntry
import com.chesire.nekomp.library.datasource.library.LibraryRepository
import com.chesire.nekomp.library.datasource.trending.TrendingItem
import com.chesire.nekomp.library.datasource.trending.TrendingRepository
import com.chesire.nekomp.library.datasource.user.UserRepository
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val WATCH_LIST_LIMIT = 20

class HomeViewModel(
    private val userRepository: UserRepository,
    private val libraryRepository: LibraryRepository,
    private val trendingRepository: TrendingRepository,
    private val showAiringSeries: ShowAiringSeriesUseCase,
    private val applicationSettings: ApplicationSettings
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.user.collect { user ->
                _uiState.update { state ->
                    state.copy(username = user.name)
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            libraryRepository.libraryEntries.collect { libraryEntries ->
                val imageQuality = applicationSettings.imageQuality.first()
                val titleLanguage = applicationSettings.titleLanguage.first()
                _uiState.update { state ->
                    state.copy(
                        watchList = libraryEntries
                            .asSequence()
                            .filter { it.type == Type.Anime }
                            .filter { it.entryStatus != EntryStatus.Completed }
                            .filter { it.progress != it.totalLength }
                            .sortedByDescending { it.updatedAt }
                            .take(WATCH_LIST_LIMIT)
                            .map { it.toWatchItem(imageQuality, titleLanguage) }
                            .toList()
                            .toPersistentList()
                    )
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            val imageQuality = applicationSettings.imageQuality.first()
            val titleLanguage = applicationSettings.titleLanguage.first()
            val trendingAnime = trendingRepository.getTrendingAnime()
            val trendingManga = trendingRepository.getTrendingManga()
            _uiState.update { state ->
                val animeItems = trendingAnime
                    .map { it.toTrendItem(imageQuality, titleLanguage) }
                    .toPersistentList()
                val mangaItems = trendingManga
                    .map { it.toTrendItem(imageQuality, titleLanguage) }
                    .toPersistentList()
                state.copy(
                    trendingAll = animeItems
                        .zip(mangaItems)
                        .flatMap { (first, second) -> listOf(first, second) }
                        .toPersistentList(),
                    trendingAnime = animeItems,
                    trendingManga = mangaItems
                )
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            showAiringSeries().collectLatest { airingItems ->
                _uiState.update { state ->
                    state.copy(
                        airing = airingItems.toPersistentList()
                    )
                }
            }
        }
    }

    fun execute(action: ViewAction) {
        when (action) {
            is ViewAction.WatchItemClick -> onWatchItemClick(action.watchItem)
            is ViewAction.WatchItemPlusOneClick -> onWatchItemPlusOneClick(action.watchItem)
            is ViewAction.AiringItemClick -> onAiringItemClick(action.airingItem)
            is ViewAction.TrendItemClick -> onTrendItemClick(action.trendItem)
            ViewAction.ObservedViewEvent -> onObservedViewEvent()
        }
    }

    private fun onWatchItemClick(watchItem: WatchItem) {
        // TODO
        // Set as the selected item
        // UI needs to navigate to a detail view
    }

    private fun onWatchItemPlusOneClick(watchItem: WatchItem) {
        // TODO: Update UI in some way?
        viewModelScope.launch(Dispatchers.IO) {
            libraryRepository.updateEntry(
                entryId = watchItem.entryId,
                newProgress = watchItem.progress + 1
            )
        }
    }

    private fun onAiringItemClick(airingItem: AiringItem) {
        // TODO
        // Set as the selected item
        // UI needs to navigate to a detail view
    }

    private fun onTrendItemClick(trendItem: TrendItem) {
        // TODO
        // Set as the selected item
        // UI needs to navigate to a detail view
    }

    private fun onObservedViewEvent() {
        _uiState.update { state ->
            state.copy(viewEvent = null)
        }
    }

    private fun LibraryEntry.toWatchItem(
        imageQuality: ImageQuality,
        titleLanguage: TitleLanguage
    ): WatchItem {
        return WatchItem(
            entryId = entryId,
            title = titles.toChosenLanguage(titleLanguage),
            posterImage = posterImage.toBestImage(imageQuality),
            progressPercent = if (totalLength == 0) {
                0f
            } else {
                (progress.toFloat() / totalLength.toFloat())
            },
            progress = progress
        )
    }

    private fun TrendingItem.toTrendItem(
        imageQuality: ImageQuality,
        titleLanguage: TitleLanguage
    ): TrendItem {
        return TrendItem(
            id = id,
            title = titles.toChosenLanguage(titleLanguage),
            posterImage = posterImage.toBestImage(imageQuality)
        )
    }
}
