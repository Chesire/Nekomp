package com.chesire.nekomp.feature.discover.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekomp.feature.discover.core.AddItemToTrackingUseCase
import com.chesire.nekomp.feature.discover.core.RetrieveLibraryUseCase
import com.chesire.nekomp.feature.discover.core.RetrieveTrendingUseCase
import com.github.michaelbull.result.onFailure
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DiscoverViewModel(
    private val retrieveLibrary: RetrieveLibraryUseCase,
    private val retrieveTrending: RetrieveTrendingUseCase,
    private val addItemToTracking: AddItemToTrackingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val trendingAnimeJob = async {
                retrieveTrending.anime()
                    .map {
                        DiscoverItem(
                            id = it.id,
                            title = it.canonicalTitle,
                            type = "Anime"
                        )
                    }
            }
            val trendingMangaJob = async {
                retrieveTrending.manga()
                    .map {
                        DiscoverItem(
                            id = it.id,
                            title = it.canonicalTitle,
                            type = "Manga"
                        )
                    }
            }

            val (trendingAnime, trendingManga) = awaitAll(trendingAnimeJob, trendingMangaJob)
            retrieveLibrary().collect {
                val ids = it.map { it.id }
                val filteredAnime = trendingAnime
                    .map {
                        it.copy(
                            isTracked = ids.contains(it.id),
                            isPendingTrack = false
                        )
                    }
                    .toPersistentList()
                val filteredManga = trendingManga
                    .map {
                        it.copy(
                            isTracked = ids.contains(it.id),
                            isPendingTrack = false
                        )
                    }
                    .toPersistentList()
                _uiState.update {
                    it.copy(
                        trendingAnime = filteredAnime,
                        trendingManga = filteredManga
                    )
                }
            }
        }
    }

    fun execute(action: ViewAction) {
        when (action) {
            ViewAction.SearchFocused -> onSearchFocused()
            is ViewAction.TrackTrendingItemClick -> onTrackTrendingItemClick(action.discoverItem)
            ViewAction.ObservedViewEvent -> onObservedViewEvent()
        }
    }

    private fun onSearchFocused() {
        // Tell UI to update?
        // Or could do this within the view itself?
    }

    private fun onTrackTrendingItemClick(discoverItem: DiscoverItem) {
        val list = if (discoverItem.type == "Anime") {
            _uiState.value.trendingAnime
        } else {
            _uiState.value.trendingManga
        }.map {
            it.copy(isPendingTrack = it.id == discoverItem.id)
        }.toPersistentList()

        _uiState.update {
            it.copy(
                trendingAnime = if (discoverItem.type == "Anime") {
                    list
                } else {
                    it.trendingAnime
                },
                trendingManga = if (discoverItem.type == "Manga") {
                    list
                } else {
                    it.trendingManga
                }
            )
        }

        viewModelScope.launch {
            addItemToTracking(discoverItem.type, discoverItem.id)
                .onFailure {
                    // TODO: Show snackbar error
                    _uiState.update {
                        it.copy(
                            trendingAnime = if (discoverItem.type == "Anime") {
                                list
                            } else {
                                it.trendingAnime
                            },
                            trendingManga = if (discoverItem.type == "Manga") {
                                list
                            } else {
                                it.trendingManga
                            }
                        )
                    }
                }
        }
    }

    private fun onObservedViewEvent() {
        _uiState.update {
            it.copy(viewEvent = null)
        }
    }
}
