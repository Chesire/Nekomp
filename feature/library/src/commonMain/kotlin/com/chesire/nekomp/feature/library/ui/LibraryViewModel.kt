package com.chesire.nekomp.feature.library.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekomp.core.model.Image
import com.chesire.nekomp.core.model.Titles
import com.chesire.nekomp.core.preferences.ApplicationSettings
import com.chesire.nekomp.core.preferences.ImageQuality
import com.chesire.nekomp.core.preferences.TitleLanguage
import com.chesire.nekomp.feature.library.core.ObserveLibraryEntriesUseCase
import com.chesire.nekomp.feature.library.core.RefreshLibraryEntriesUseCase
import com.chesire.nekomp.feature.library.data.LibrarySettings
import com.chesire.nekomp.feature.library.data.ViewType
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val refreshLibraryEntries: RefreshLibraryEntriesUseCase,
    private val observeLibraryEntries: ObserveLibraryEntriesUseCase,
    private val applicationSettings: ApplicationSettings,
    private val librarySettings: LibrarySettings
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            refreshLibraryEntries()
        }
        viewModelScope.launch {
            observeLibraryEntries().collect { entries ->
                val imageQuality = applicationSettings.imageQuality.first()
                val titleLanguage = applicationSettings.titleLanguage.first()
                _uiState.update { state ->
                    state.copy(
                        entries = entries
                            .map {
                                Entry(
                                    id = it.id,
                                    title = it.titles.toChosenLanguage(titleLanguage),
                                    posterImage = it.posterImage.toBestImage(imageQuality),
                                    coverImage = it.coverImage.toBestImage(imageQuality)
                                )
                            }
                            .toImmutableList()
                    )
                }
            }
        }
        viewModelScope.launch {
            librarySettings.viewType.collect { viewType ->
                _uiState.update { state ->
                    state.copy(viewType = viewType)
                }
            }
        }
    }

    fun execute(action: ViewAction) {
        when (action) {
            ViewAction.ViewTypeClick -> onViewTypeClick()
            is ViewAction.ViewTypeChosen -> onViewTypeChosen(action.newType)
            ViewAction.ObservedViewEvent -> onObservedViewEvent()
        }
    }

    private fun onViewTypeClick() = viewModelScope.launch {
        _uiState.update { state ->
            state.copy(
                bottomSheet = LibraryBottomSheet.ViewTypeBottomSheet(
                    types = ViewType.entries.toPersistentList(),
                    selectedType = librarySettings.viewType.first()
                )
            )
        }
    }

    private fun onViewTypeChosen(newViewType: ViewType?) = viewModelScope.launch {
        if (newViewType != null) {
            librarySettings.updateViewType(newViewType)
        }

        _uiState.update { state ->
            state.copy(bottomSheet = null)
        }
    }

    private fun onObservedViewEvent() {
        _uiState.update {
            it.copy(viewEvent = null)
        }
    }

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
