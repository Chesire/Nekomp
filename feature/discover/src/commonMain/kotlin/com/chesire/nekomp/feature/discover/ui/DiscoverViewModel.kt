package com.chesire.nekomp.feature.discover.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekomp.core.database.dao.MappingDao
import com.chesire.nekomp.core.ext.toBestImage
import com.chesire.nekomp.core.ext.toChosenLanguage
import com.chesire.nekomp.core.preferences.ApplicationSettings
import com.chesire.nekomp.feature.discover.core.AddItemToTrackingUseCase
import com.chesire.nekomp.feature.discover.core.DeleteItemUseCase
import com.chesire.nekomp.feature.discover.core.RecentSearchesUseCase
import com.chesire.nekomp.feature.discover.core.RetrieveLibraryUseCase
import com.chesire.nekomp.feature.discover.core.RetrieveTrendingDataUseCase
import com.chesire.nekomp.feature.discover.core.SearchForUseCase
import com.chesire.nekomp.library.datasource.library.LibraryEntry
import com.chesire.nekomp.library.datasource.search.SearchItem
import com.chesire.nekomp.library.datasource.trending.TrendingItem
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Suppress("TooManyFunctions")
class DiscoverViewModel(
    private val retrieveLibrary: RetrieveLibraryUseCase,
    private val retrieveTrendingData: RetrieveTrendingDataUseCase,
    private val addItemToTracking: AddItemToTrackingUseCase,
    private val deleteItem: DeleteItemUseCase,
    private val recentSearches: RecentSearchesUseCase,
    private val searchFor: SearchForUseCase,
    private val mappingDao: MappingDao,
    private val applicationSettings: ApplicationSettings
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()
    private var _lastSearch = ""
    private var _libraryItems: List<LibraryEntry> = emptyList()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val trendingData = retrieveTrendingData()
            retrieveLibrary().collect { libraryItems ->
                _libraryItems = libraryItems
                val trendingAnime = trendingData
                    .trendingAnime
                    .map { it.toDiscoverItem() }
                    .toPersistentList()
                val trendingManga = trendingData
                    .trendingManga
                    .map { it.toDiscoverItem() }
                    .toPersistentList()
                val topRatedAnime = trendingData
                    .topRatedAnime
                    .map { it.toDiscoverItem() }
                    .toPersistentList()
                val topRatedManga = trendingData
                    .topRatedManga
                    .map { it.toDiscoverItem() }
                    .toPersistentList()
                val mostPopularAnime = trendingData
                    .mostPopularAnime
                    .map { it.toDiscoverItem() }
                    .toPersistentList()
                val mostPopularManga = trendingData
                    .mostPopularManga
                    .map { it.toDiscoverItem() }
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
        viewModelScope.launch(Dispatchers.IO) {
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
            is ViewAction.UntrackItemClick -> onUntrackItemClick(action.discoverItem)
            is ViewAction.WebViewClick -> onWebViewClick(action.discoverItem, action.webViewType)
            ViewAction.ObservedViewEvent -> onObservedViewEvent()
        }
    }

    private fun onWebViewClick(discoverItem: DiscoverItem, type: WebViewType) {
        val url = when (type) {
            WebViewType.Kitsu -> "https://kitsu.app/${discoverItem.type.name.lowercase()}/${discoverItem.kitsuId}"
            WebViewType.MyAnimeList -> "https://myanimelist.net/${discoverItem.type.name.lowercase()}/${discoverItem.malId}"
            WebViewType.AniList -> "https://anilist.co/${discoverItem.type.name.lowercase()}/${discoverItem.aniListId}"
        }
        _uiState.update { state ->
            state.copy(
                viewEvent = ViewEvent.OpenWebView(url = url)
            )
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
            searchFor(title = searchTerm)
                .onSuccess { searchItems ->
                    _uiState.update { state ->
                        state.copy(
                            resultsState = state.resultsState.copy(
                                searchResults = searchItems
                                    .map { item -> item.toDiscoverItem() }
                                    .toPersistentList()
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
            if (state.detailState.currentItem?.kitsuId != discoverItem.kitsuId) {
                return
            }
            state.copy(
                detailState = state.detailState.copy(
                    currentItem = state.detailState.currentItem.copy(isPendingTrack = true)
                )
            )
        }

        viewModelScope.launch {
            val result = addItemToTracking(discoverItem.type, discoverItem.kitsuId)

            // TODO: Snow snackbar error
            _uiState.update { state ->
                if (state.detailState.currentItem?.kitsuId != discoverItem.kitsuId) {
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

    private fun onUntrackItemClick(discoverItem: DiscoverItem) {
        val entryId = discoverItem.entryId
        _uiState.update { state ->
            if (state.detailState.currentItem?.kitsuId != discoverItem.kitsuId || entryId == null) {
                return
            }
            state.copy(
                detailState = state.detailState.copy(
                    currentItem = state.detailState.currentItem.copy(isPendingTrack = true)
                )
            )
        }
        viewModelScope.launch {
            deleteItem(entryId!!)
                .onFailure {
                    _uiState.update { state ->
                        state.copy(
                            viewEvent = ViewEvent.ShowFailure("Failed to delete, please try again")
                        )
                    }
                }
        }.invokeOnCompletion {
            _uiState.update { state ->
                state.copy(
                    detailState = state.detailState.copy(
                        currentItem = state.detailState.currentItem?.copy(isPendingTrack = false)
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

    private suspend fun TrendingItem.toDiscoverItem(): DiscoverItem {
        val imageQuality = applicationSettings.imageQuality.first()
        val titleLanguage = applicationSettings.titleLanguage.first()
        val mapper = mappingDao.entityFromKitsuId(id)
        return DiscoverItem(
            entryId = _libraryItems.find { it.id == id }?.entryId,
            kitsuId = id,
            malId = mapper?.malId,
            aniListId = mapper?.aniListId,
            title = titles.toChosenLanguage(titleLanguage),
            type = type,
            subType = subtype,
            status = status,
            synopsis = synopsis,
            averageRating = averageRating,
            totalLength = totalLength,
            coverImage = coverImage.toBestImage(imageQuality),
            posterImage = posterImage.toBestImage(imageQuality),
            isTracked = _libraryItems.any { it.id == id }
        )
    }

    private suspend fun SearchItem.toDiscoverItem(): DiscoverItem {
        val imageQuality = applicationSettings.imageQuality.first()
        val titleLanguage = applicationSettings.titleLanguage.first()
        val mapper = mappingDao.entityFromKitsuId(id)
        return DiscoverItem(
            entryId = _libraryItems.find { it.id == id }?.entryId,
            kitsuId = id,
            malId = mapper?.malId,
            aniListId = mapper?.aniListId,
            title = titles.toChosenLanguage(titleLanguage),
            type = type,
            subType = subtype,
            status = status,
            synopsis = synopsis,
            averageRating = averageRating,
            totalLength = totalLength,
            coverImage = coverImage.toBestImage(imageQuality),
            posterImage = posterImage.toBestImage(imageQuality),
            isTracked = _libraryItems.any { it.id == id }
        )
    }
}

enum class WebViewType {
    Kitsu,
    MyAnimeList,
    AniList
}
