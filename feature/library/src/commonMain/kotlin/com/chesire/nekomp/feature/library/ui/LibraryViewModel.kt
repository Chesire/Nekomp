package com.chesire.nekomp.feature.library.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekomp.core.model.Image
import com.chesire.nekomp.core.model.Titles
import com.chesire.nekomp.core.model.Type
import com.chesire.nekomp.core.preferences.ApplicationSettings
import com.chesire.nekomp.core.preferences.ImageQuality
import com.chesire.nekomp.core.preferences.TitleLanguage
import com.chesire.nekomp.feature.library.core.ObserveLibraryEntriesUseCase
import com.chesire.nekomp.feature.library.core.RefreshLibraryEntriesUseCase
import com.chesire.nekomp.feature.library.data.LibrarySettings
import com.chesire.nekomp.feature.library.data.SortChoice
import com.chesire.nekomp.feature.library.data.ViewType
import com.chesire.nekomp.library.datasource.library.LibraryEntry
import com.chesire.nekomp.library.datasource.library.LibraryRepository
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val refreshLibraryEntries: RefreshLibraryEntriesUseCase,
    private val observeLibraryEntries: ObserveLibraryEntriesUseCase,
    private val libraryRepository: LibraryRepository,
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
            val imageQuality = applicationSettings.imageQuality.first()
            val titleLanguage = applicationSettings.titleLanguage.first()
            val libraryEntriesFlow = observeLibraryEntries()
            val typeFilterFlow = librarySettings.typeFilter
            val sortFlow = librarySettings.sortChoice
            libraryEntriesFlow
                .combine(typeFilterFlow) { entries, filter ->
                    entries.filter { entry ->
                        filter.getOrElse(entry.type) { true }
                    }
                }
                .combine(sortFlow) { entries, sort ->
                    entries.sortedBy { entry ->
                        when (sort) {
                            SortChoice.Default -> entry.entryId.toString()
                            SortChoice.Title -> entry.titles.toChosenLanguage(titleLanguage)
                            SortChoice.StartDate -> entry.startDate
                            SortChoice.EndDate -> entry.endDate
                            SortChoice.Rating -> entry.rating.toString()
                        }
                    }
                }
                .collect { entries ->
                    _uiState.update { state ->
                        state.copy(
                            entries = entries
                                .map { it.toEntry(imageQuality, titleLanguage) }
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
        viewModelScope.launch {
            librarySettings.typeFilter.collect { displayTypeFilter ->
                _uiState.update { state ->
                    state.copy(typeFilters = displayTypeFilter.toPersistentMap())
                }
            }
        }
    }

    fun execute(action: ViewAction) {
        when (action) {
            ViewAction.ViewTypeClick -> onViewTypeClick()
            is ViewAction.ViewTypeChosen -> onViewTypeChosen(action.newType)

            ViewAction.SortClick -> onSortChoiceClick()
            is ViewAction.SortChosen -> onSortChoiceChosen(action.newSortChoice)

            is ViewAction.TypeFilterClick -> onTypeFilterClick(action.selectedType)

            is ViewAction.ItemPlusOneClick -> onItemPlusOneClick(action.entry)

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

    private fun onSortChoiceClick() = viewModelScope.launch {
        _uiState.update { state ->
            state.copy(
                bottomSheet = LibraryBottomSheet.SortBottomSheet(
                    options = SortChoice.entries.toPersistentList(),
                    selectedOption = librarySettings.sortChoice.first()
                )
            )
        }
    }

    private fun onSortChoiceChosen(newSortChoice: SortChoice?) = viewModelScope.launch {
        if (newSortChoice != null) {
            librarySettings.updateSortChoice(newSortChoice)
        }

        _uiState.update { state ->
            state.copy(bottomSheet = null)
        }
    }

    private fun onTypeFilterClick(selectedType: Type) = viewModelScope.launch {
        val newMap = _uiState.value.typeFilters.map {
            if (it.key == selectedType) {
                it.key to !it.value
            } else {
                it.key to it.value
            }
        }.toMap()

        librarySettings.updateTypeFilter(newMap)
    }

    private fun onItemPlusOneClick(entry: Entry) = viewModelScope.launch(Dispatchers.IO) {
        // TODO: Update UI in some way?
        libraryRepository.updateEntry(
            entryId = entry.entryId,
            newProgress = entry.progress + 1
        )
    }

    private fun onObservedViewEvent() {
        _uiState.update {
            it.copy(viewEvent = null)
        }
    }

    private fun LibraryEntry.toEntry(
        imageQuality: ImageQuality,
        titleLanguage: TitleLanguage
    ): Entry {
        return Entry(
            entryId = entryId,
            title = titles.toChosenLanguage(titleLanguage),
            posterImage = posterImage.toBestImage(imageQuality),
            coverImage = coverImage.toBestImage(imageQuality),
            progressPercent = if (totalLength == 0) {
                0f
            } else {
                (progress.toFloat() / totalLength.toFloat())
            },
            progress = progress
        )
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
