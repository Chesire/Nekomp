package com.chesire.nekomp.feature.discover.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekomp.core.coroutines.combine
import com.chesire.nekomp.core.database.dao.MappingDao
import com.chesire.nekomp.core.ext.toBestImage
import com.chesire.nekomp.core.ext.toChosenLanguage
import com.chesire.nekomp.core.preferences.ApplicationSettings
import com.chesire.nekomp.core.preferences.models.ImageQuality
import com.chesire.nekomp.core.preferences.models.TitleLanguage
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
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Suppress("TooManyFunctions", "LongParameterList")
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

    private val _detailState = MutableStateFlow(DetailState())
    private val _trendingState = MutableStateFlow(TrendingState())
    private val _recentSearches = MutableStateFlow<ImmutableList<String>>(persistentListOf())
    private val _searchTerm = MutableStateFlow("")
    private val _resultsState = MutableStateFlow(ResultsState())
    private val _viewEvent = MutableStateFlow<ViewEvent?>(null)

    val uiState = combine(
        _detailState,
        _trendingState,
        _recentSearches,
        _searchTerm,
        _resultsState,
        _viewEvent
    ) { detail, trending, recent, searchTerm, results, viewEvent ->
        UIState(
            searchTerm = searchTerm,
            recentSearches = recent,
            trendingState = trending,
            resultsState = results,
            detailState = detail,
            viewEvent = viewEvent
        )
    }.onStart {
        collectLibrary()
    }.onStart {
        collectRecents()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UIState()
    )
    private var _lastSearch = ""
    private var _libraryItems: List<LibraryEntry> = emptyList()
    private lateinit var _imageQuality: ImageQuality
    private lateinit var _titleLanguage: TitleLanguage

    private fun collectLibrary() {
        viewModelScope.launch(Dispatchers.IO) {
            val trendingData = retrieveTrendingData()
            _imageQuality = applicationSettings.imageQuality.first()
            _titleLanguage = applicationSettings.titleLanguage.first()
            retrieveLibrary().collect { libraryItems ->
                _libraryItems = libraryItems
                updateDetailState(libraryItems)
                updateTrendingState(trendingData)
            }
        }
    }

    private fun updateDetailState(libraryItems: List<LibraryEntry>) {
        _detailState.update { detailState ->
            detailState.copy(
                currentItem = detailState.currentItem?.copy(
                    entryId = libraryItems.find { it.id == detailState.currentItem.kitsuId }?.entryId,
                    isTracked = libraryItems.any { it.id == detailState.currentItem.kitsuId }
                )
            )
        }
    }

    private suspend fun updateTrendingState(trendingData: RetrieveTrendingDataUseCase.TrendingData) {
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

        _trendingState.update { trendingState ->
            TrendingState(
                trendingAnime = trendingAnime,
                trendingManga = trendingManga,
                topRatedAnime = topRatedAnime,
                topRatedManga = topRatedManga,
                mostPopularAnime = mostPopularAnime,
                mostPopularManga = mostPopularManga
            )
        }
    }

    private fun collectRecents() {
        viewModelScope.launch(Dispatchers.IO) {
            recentSearches.recents.collect { recents ->
                _recentSearches.update { recents.reversed().toPersistentList() }
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

    private fun onSearchTextUpdated(newSearchText: String) {
        _searchTerm.update { newSearchText }
    }

    private fun onSearchExecuted() {
        val searchTerm = _searchTerm.value
        if (searchTerm == _lastSearch && _resultsState.value.searchResults.isNotEmpty()) {
            // Just exit, we have data
            return
        } else if (searchTerm.isBlank()) {
            _resultsState.update {
                it.copy(searchResults = persistentListOf())
            }
            return
        }

        // TODO: Handle UI updating for search execution
        viewModelScope.launch {
            _lastSearch = searchTerm
            recentSearches.addRecentSearch(searchTerm)
            searchFor(title = searchTerm)
                .onSuccess { searchItems ->
                    _resultsState.update {
                        it.copy(
                            searchResults = searchItems
                                .map { item -> item.toDiscoverItem() }
                                .toPersistentList()
                        )
                    }
                }
                .onFailure {
                    _resultsState.update {
                        it.copy(searchResults = persistentListOf())
                    }
                    _viewEvent.update { ViewEvent.ShowFailure("Search was unsuccessful") }
                }
        }
    }

    private fun onRecentSearchClick(recentSearchTerm: String) {
        _searchTerm.update { recentSearchTerm }
    }

    private fun onItemSelect(discoverItem: DiscoverItem) {
        _detailState.update {
            it.copy(currentItem = discoverItem)
        }
    }

    private fun onTrackItemClick(discoverItem: DiscoverItem) {
        if (_detailState.value.currentItem?.kitsuId != discoverItem.kitsuId) {
            return
        }
        _detailState.update {
            it.copy(currentItem = it.currentItem?.copy(isPendingTrack = true))
        }

        viewModelScope.launch {
            addItemToTracking(discoverItem.type, discoverItem.kitsuId)
                .onFailure {
                    _viewEvent.update { ViewEvent.ShowFailure("Failed to track, please try again") }
                }
        }.invokeOnCompletion {
            _detailState.update {
                it.copy(currentItem = it.currentItem?.copy(isPendingTrack = false))
            }
        }
    }

    private fun onUntrackItemClick(discoverItem: DiscoverItem) {
        val entryId = discoverItem.entryId
        if (_detailState.value.currentItem?.kitsuId != discoverItem.kitsuId || entryId == null) {
            return
        }
        _detailState.update {
            it.copy(currentItem = it.currentItem?.copy(isPendingTrack = true))
        }

        viewModelScope.launch {
            deleteItem(entryId)
                .onFailure {
                    _viewEvent.update { ViewEvent.ShowFailure("Failed to remove tracking, please try again") }
                }
        }.invokeOnCompletion {
            _detailState.update {
                it.copy(currentItem = it.currentItem?.copy(isPendingTrack = false))
            }
        }
    }

    private fun onWebViewClick(discoverItem: DiscoverItem, type: WebViewType) {
        val id = when (type) {
            WebViewType.Kitsu -> discoverItem.kitsuId
            WebViewType.MyAnimeList -> discoverItem.malId
            WebViewType.AniList -> discoverItem.aniListId
        }
        val url = type.buildWebUrl(discoverItem.type.name.lowercase(), id)
        _viewEvent.update { ViewEvent.OpenWebView(url) }
    }

    private fun onObservedViewEvent() {
        _viewEvent.update { null }
    }

    private suspend fun TrendingItem.toDiscoverItem(): DiscoverItem {
        val mapper = mappingDao.entityFromKitsuId(id)
        val matchingId = _libraryItems.find { it.id == id }?.entryId
        return DiscoverItem(
            entryId = matchingId,
            kitsuId = id,
            malId = mapper?.malId,
            aniListId = mapper?.aniListId,
            title = titles.toChosenLanguage(_titleLanguage),
            type = type,
            subType = subtype,
            status = status,
            synopsis = synopsis,
            averageRating = averageRating,
            totalLength = totalLength,
            coverImage = coverImage
                .toBestImage(_imageQuality)
                .ifBlank { posterImage.toBestImage(_imageQuality) },
            posterImage = posterImage
                .toBestImage(_imageQuality)
                .ifBlank { coverImage.toBestImage(_imageQuality) },
            isTracked = matchingId != null
        )
    }

    private suspend fun SearchItem.toDiscoverItem(): DiscoverItem {
        val mapper = mappingDao.entityFromKitsuId(id)
        val matchingId = _libraryItems.find { it.id == id }?.entryId
        return DiscoverItem(
            entryId = matchingId,
            kitsuId = id,
            malId = mapper?.malId,
            aniListId = mapper?.aniListId,
            title = titles.toChosenLanguage(_titleLanguage),
            type = type,
            subType = subtype,
            status = status,
            synopsis = synopsis,
            averageRating = averageRating,
            totalLength = totalLength,
            coverImage = coverImage
                .toBestImage(_imageQuality)
                .ifBlank { posterImage.toBestImage(_imageQuality) },
            posterImage = posterImage
                .toBestImage(_imageQuality)
                .ifBlank { coverImage.toBestImage(_imageQuality) },
            isTracked = matchingId != null
        )
    }
}

enum class WebViewType(val address: String) {
    Kitsu("https://kitsu.app"),
    MyAnimeList("https://myanimelist.net"),
    AniList("https://anilist.co");

    fun buildWebUrl(type: String, id: Int?): String {
        return "$address/$type/${id ?: ""}"
    }
}
