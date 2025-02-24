package com.chesire.nekomp.feature.discover.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekomp.core.model.Type
import com.chesire.nekomp.feature.discover.core.AddItemToTrackingUseCase
import com.chesire.nekomp.feature.discover.core.RecentSearchesUseCase
import com.chesire.nekomp.feature.discover.core.RetrieveLibraryUseCase
import com.chesire.nekomp.feature.discover.core.RetrieveTrendingDataUseCase
import com.chesire.nekomp.feature.discover.core.SearchForUseCase
import com.chesire.nekomp.library.datasource.trending.TrendingItem
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DiscoverViewModel(
    private val retrieveLibrary: RetrieveLibraryUseCase,
    private val retrieveTrendingData: RetrieveTrendingDataUseCase,
    private val addItemToTracking: AddItemToTrackingUseCase,
    private val recentSearches: RecentSearchesUseCase,
    private val searchFor: SearchForUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()
    private var _lastSearch = ""

    init {
        viewModelScope.launch {
            val trendingData = retrieveTrendingData()
            retrieveLibrary().collect {
                val ids = it.map { it.id }
                val trendingAnime = trendingData
                    .trendingAnime
                    .map { it.toDiscoverItem(ids.contains(it.id)) }
                    .toPersistentList()
                val trendingManga = trendingData
                    .trendingManga
                    .map { it.toDiscoverItem(ids.contains(it.id)) }
                    .toPersistentList()
                val topRatedAnime = trendingData
                    .topRatedAnime
                    .map { it.toDiscoverItem(ids.contains(it.id)) }
                    .toPersistentList()
                val topRatedManga = trendingData
                    .topRatedManga
                    .map { it.toDiscoverItem(ids.contains(it.id)) }
                    .toPersistentList()
                val mostPopularAnime = trendingData
                    .mostPopularAnime
                    .map { it.toDiscoverItem(ids.contains(it.id)) }
                    .toPersistentList()
                val mostPopularManga = trendingData
                    .mostPopularManga
                    .map { it.toDiscoverItem(ids.contains(it.id)) }
                    .toPersistentList()
                _uiState.update {
                    it.copy(
                        trendingAnime = trendingAnime,
                        trendingManga = trendingManga,
                        topRatedAnime = topRatedAnime,
                        topRatedManga = topRatedManga,
                        mostPopularAnime = mostPopularAnime,
                        mostPopularManga = mostPopularManga
                    )
                }
            }
        }
        viewModelScope.launch {
            recentSearches.recents.collect { recents ->
                _uiState.update {
                    it.copy(recentSearches = recents.reversed().toPersistentList())
                }
            }
        }
    }

    fun execute(action: ViewAction) {
        when (action) {
            is ViewAction.SearchTextUpdated -> onSearchTextUpdated(action.newSearchText)
            ViewAction.SearchExecute -> onSearchExecuted()
            is ViewAction.RecentSearchClick -> onRecentSearchClick(action.recentSearchTerm)
            is ViewAction.TrackItemClick -> onTrackItemClick(action.discoverItem)
            ViewAction.ObservedViewEvent -> onObservedViewEvent()
        }
    }

    private fun onSearchTextUpdated(newSearchText: String) {
        _uiState.update {
            it.copy(searchTerm = newSearchText)
        }
    }

    private fun onSearchExecuted() {
        val searchTerm = _uiState.value.searchTerm
        if (searchTerm == _lastSearch && _uiState.value.searchResults.isNotEmpty()) {
            // Just exit, we have data
            return
        } else if (searchTerm.isBlank()) {
            _uiState.update { state ->
                state.copy(searchResults = persistentListOf())
            }
            return
        }

        // TODO: Handle UI updating for search execution
        viewModelScope.launch {
            _lastSearch = searchTerm
            recentSearches.addRecentSearch(searchTerm)
            searchFor(searchTerm)
                .onSuccess { searchItems ->
                    _uiState.update { state ->
                        state.copy(
                            searchResults = searchItems.map { item ->
                                DiscoverItem(
                                    id = item.id,
                                    title = item.canonicalTitle,
                                    type = item.type,
                                    coverImage = item.coverImage,
                                    posterImage = item.posterImage,
                                    isTracked = false // TODO
                                )
                            }.toPersistentList()
                        )
                    }
                }
                .onFailure {
                    _uiState.update { state ->
                        state.copy(
                            searchResults = persistentListOf(),
                            viewEvent = ViewEvent.ShowFailure("Search was unsuccessful")
                        )
                    }
                }
        }
    }

    private fun onRecentSearchClick(recentSearchTerm: String) {
        _uiState.update {
            it.copy(searchTerm = recentSearchTerm)
        }
    }

    private fun onTrackItemClick(discoverItem: DiscoverItem) {
        val list = if (discoverItem.type == Type.Anime) {
            _uiState.value.trendingAnime
        } else {
            _uiState.value.trendingManga
        }.map {
            it.copy(isPendingTrack = it.id == discoverItem.id)
        }.toPersistentList()

        _uiState.update {
            it.copy(
                trendingAnime = if (discoverItem.type == Type.Anime) {
                    list
                } else {
                    it.trendingAnime
                },
                trendingManga = if (discoverItem.type == Type.Manga) {
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
                            trendingAnime = if (discoverItem.type == Type.Anime) {
                                list
                            } else {
                                it.trendingAnime
                            },
                            trendingManga = if (discoverItem.type == Type.Manga) {
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

    private fun TrendingItem.toDiscoverItem(isTracked: Boolean): DiscoverItem {
        return DiscoverItem(
            id = id,
            title = canonicalTitle,
            type = type,
            coverImage = coverImage,
            posterImage = posterImage,
            isTracked = isTracked
        )
    }
}
