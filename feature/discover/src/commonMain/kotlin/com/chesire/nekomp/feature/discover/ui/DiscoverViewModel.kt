package com.chesire.nekomp.feature.discover.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private var _libraryIds: Set<Int> = emptySet() // TODO: Find better way to handle this

    init {
        viewModelScope.launch {
            val trendingData = retrieveTrendingData()
            retrieveLibrary().collect { libraryItems ->
                _libraryIds = libraryItems.map { it.id }.toSet()
                val trendingAnime = trendingData
                    .trendingAnime
                    .map { it.toDiscoverItem(_libraryIds.contains(it.id)) }
                    .toPersistentList()
                val trendingManga = trendingData
                    .trendingManga
                    .map { it.toDiscoverItem(_libraryIds.contains(it.id)) }
                    .toPersistentList()
                val topRatedAnime = trendingData
                    .topRatedAnime
                    .map { it.toDiscoverItem(_libraryIds.contains(it.id)) }
                    .toPersistentList()
                val topRatedManga = trendingData
                    .topRatedManga
                    .map { it.toDiscoverItem(_libraryIds.contains(it.id)) }
                    .toPersistentList()
                val mostPopularAnime = trendingData
                    .mostPopularAnime
                    .map { it.toDiscoverItem(_libraryIds.contains(it.id)) }
                    .toPersistentList()
                val mostPopularManga = trendingData
                    .mostPopularManga
                    .map { it.toDiscoverItem(_libraryIds.contains(it.id)) }
                    .toPersistentList()

                _uiState.update { state ->
                    state.copy(
                        trendingState = state.trendingState.copy(
                            trendingAnime = trendingAnime,
                            trendingManga = trendingManga,
                            topRatedAnime = topRatedAnime,
                            topRatedManga = topRatedManga,
                            mostPopularAnime = mostPopularAnime,
                            mostPopularManga = mostPopularManga
                        )
                    )
                }
            }
        }
        viewModelScope.launch {
            recentSearches.recents.collect { recents ->
                _uiState.update { state ->
                    state.copy(recentSearches = recents.reversed().toPersistentList())
                }
            }
        }
    }

    fun execute(action: ViewAction) {
        when (action) {
            is ViewAction.SearchTextUpdated -> onSearchTextUpdated(action.newSearchText)
            ViewAction.SearchExecute -> onSearchExecuted()
            is ViewAction.RecentSearchClick -> onRecentSearchClick(action.recentSearchTerm)
            is ViewAction.ItemSelect -> onItemSelect(action.discoverItem)
            is ViewAction.TrackItemClick -> onTrackItemClick(action.discoverItem)
            ViewAction.ObservedViewEvent -> onObservedViewEvent()
        }
    }

    private fun onSearchTextUpdated(newSearchText: String) {
        _uiState.update { state ->
            state.copy(searchTerm = newSearchText)
        }
    }

    private fun onSearchExecuted() {
        val searchTerm = _uiState.value.searchTerm
        if (searchTerm == _lastSearch && _uiState.value.resultsState.searchResults.isNotEmpty()) {
            // Just exit, we have data
            return
        } else if (searchTerm.isBlank()) {
            _uiState.update { state ->
                state.copy(
                    resultsState = state.resultsState.copy(searchResults = persistentListOf())
                )
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
                            resultsState = state.resultsState.copy(
                                searchResults = searchItems.map { item ->
                                    DiscoverItem(
                                        id = item.id,
                                        title = item.canonicalTitle,
                                        type = item.type,
                                        coverImage = item.coverImage,
                                        posterImage = item.posterImage,
                                        isTracked = _libraryIds.contains(item.id)
                                    )
                                }.toPersistentList()
                            )
                        )
                    }
                }
                .onFailure {
                    _uiState.update { state ->
                        state.copy(
                            resultsState = state.resultsState.copy(searchResults = persistentListOf()),
                            viewEvent = ViewEvent.ShowFailure("Search was unsuccessful")
                        )
                    }
                }
        }
    }

    private fun onRecentSearchClick(recentSearchTerm: String) {
        _uiState.update { state ->
            state.copy(searchTerm = recentSearchTerm)
        }
    }

    private fun onItemSelect(discoverItem: DiscoverItem) {
        _uiState.update { state ->
            state.copy(detailState = state.detailState.copy(currentItem = discoverItem))
        }
    }

    private fun onTrackItemClick(discoverItem: DiscoverItem) {
        _uiState.update { state ->
            if (state.detailState.currentItem?.id != discoverItem.id) {
                return
            }
            state.copy(
                detailState = state.detailState.copy(
                    currentItem = state.detailState.currentItem.copy(isPendingTrack = true)
                )
            )
        }

        viewModelScope.launch {
            val result = addItemToTracking(discoverItem.type, discoverItem.id)

            // TODO: Snow snackbar error
            _uiState.update { state ->
                if (state.detailState.currentItem?.id != discoverItem.id) {
                    // Ignore it, the ui has changed
                    return@launch
                }
                state.copy(
                    detailState = state.detailState.copy(
                        currentItem = state
                            .detailState
                            .currentItem
                            .copy(
                                isPendingTrack = false,
                                isTracked = result.isOk
                            )
                    )
                )
            }
        }
    }

    private fun onObservedViewEvent() {
        _uiState.update { state ->
            state.copy(viewEvent = null)
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
