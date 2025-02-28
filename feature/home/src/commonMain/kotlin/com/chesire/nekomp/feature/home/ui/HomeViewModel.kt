package com.chesire.nekomp.feature.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekomp.core.model.EntryStatus
import com.chesire.nekomp.core.model.Image
import com.chesire.nekomp.core.model.Titles
import com.chesire.nekomp.core.model.Type
import com.chesire.nekomp.core.preferences.ApplicationSettings
import com.chesire.nekomp.core.preferences.ImageQuality
import com.chesire.nekomp.core.preferences.TitleLanguage
import com.chesire.nekomp.library.datasource.library.LibraryRepository
import com.chesire.nekomp.library.datasource.user.UserRepository
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userRepository: UserRepository,
    private val libraryRepository: LibraryRepository,
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
                            .filter { it.type == Type.Anime }
                            .filter { it.entryStatus != EntryStatus.Completed }
                            .sortedBy { it.updatedAt }
                            .take(10)
                            .map { libraryEntry ->
                                WatchItem(
                                    id = libraryEntry.id,
                                    title = libraryEntry.titles.toChosenLanguage(titleLanguage),
                                    posterImage = libraryEntry.posterImage.toBestImage(imageQuality),
                                    progress = if (libraryEntry.totalLength == 0) {
                                        0f
                                    } else {
                                        (libraryEntry.progress.toFloat() / libraryEntry.totalLength.toFloat())
                                    }
                                )
                            }
                            .toPersistentList()
                    )
                }
            }
        }
        // TODO: Pull the current trending
    }

    fun execute(action: ViewAction) {
        when (action) {
            is ViewAction.WatchItemClick -> onWatchItemClick(action.watchItem)
            is ViewAction.WatchItemPlusOneClick -> onWatchItemPlusOneClick(action.watchItem)
            ViewAction.ObservedViewEvent -> onObservedViewEvent()
        }
    }

    private fun onWatchItemClick(watchItem: WatchItem) {
        // TODO
        // Set as the selected item
        // UI needs to navigate to a detail view
    }

    private fun onWatchItemPlusOneClick(watchItem: WatchItem) {
        // TODO
        // Increment the progress by one and send to api
    }

    private fun onObservedViewEvent() {
        _uiState.update { state ->
            state.copy(viewEvent = null)
        }
    }

    // TODO: Move these with the other implementations somewhere
    private fun Titles.toChosenLanguage(titleLanguage: TitleLanguage): String {
        return when (titleLanguage) {
            TitleLanguage.Canonical -> canonical
            TitleLanguage.English -> english.ifBlank { canonical }
            TitleLanguage.Romaji -> romaji.ifBlank { canonical }
            TitleLanguage.CJK -> cjk.ifBlank { canonical }
        }
    }

    private fun Image.toBestImage(imageQuality: ImageQuality): String {
        return when (imageQuality) {
            ImageQuality.Lowest -> lowest
            ImageQuality.Low -> low
            ImageQuality.Medium -> middle
            ImageQuality.High -> high
            ImageQuality.Highest -> highest
        }
    }
}
