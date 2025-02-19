package com.chesire.nekomp.feature.discover.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekomp.feature.discover.core.AddItemToTrackingUseCase
import com.chesire.nekomp.feature.discover.core.RetrieveTrendingUseCase
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DiscoverViewModel(
    private val retrieveTrending: RetrieveTrendingUseCase,
    private val addItemToTracking: AddItemToTrackingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val animeJob = async {
                val models = retrieveTrending.anime()
                    .map {
                        DiscoverItem(
                            id = it.id,
                            title = it.canonicalTitle,
                            type = "Anime"
                        )
                    }

                _uiState.update {
                    it.copy(trendingAnime = models.toPersistentList())
                }
            }
            val mangaJob = async {
                val models = retrieveTrending.manga()
                    .map {
                        DiscoverItem(
                            id = it.id,
                            title = it.canonicalTitle,
                            type = "Manga"
                        )
                    }

                _uiState.update {
                    it.copy(trendingManga = models.toPersistentList())
                }
            }

            awaitAll(animeJob, mangaJob)

            // now collect flow for library entries?
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
        // TODO: Ui stuff
        viewModelScope.launch {
            addItemToTracking(discoverItem.type, discoverItem.id)
        }
    }

    private fun onObservedViewEvent() {
        _uiState.update {
            it.copy(viewEvent = null)
        }
    }
}
