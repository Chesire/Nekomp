package com.chesire.nekomp.feature.library.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekomp.core.ext.toBestImage
import com.chesire.nekomp.core.ext.toChosenLanguage
import com.chesire.nekomp.core.model.EntryStatus
import com.chesire.nekomp.core.model.Type
import com.chesire.nekomp.core.preferences.ApplicationSettings
import com.chesire.nekomp.core.preferences.models.ImageQuality
import com.chesire.nekomp.core.preferences.models.TitleLanguage
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

@Suppress("TooManyFunctions")
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
            val statusFilterFlow = librarySettings.statusFilter
            val sortFlow = librarySettings.sortChoice
            libraryEntriesFlow
                .combine(typeFilterFlow) { entries, filter ->
                    entries.filter { entry ->
                        filter.getOrElse(entry.type) { true }
                    }
                }
                .combine(statusFilterFlow) { entries, filter ->
                    entries.filter { entry ->
                        filter.getOrElse(entry.entryStatus) { true }
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
            librarySettings.typeFilter.collect { typeFilter ->
                _uiState.update { state ->
                    state.copy(typeFilters = typeFilter.toPersistentMap())
                }
            }
        }
        viewModelScope.launch {
            librarySettings.statusFilter.collect { statusFilter ->
                _uiState.update { state ->
                    state.copy(statusFilters = statusFilter.toPersistentMap())
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
            is ViewAction.StatusFilterClick -> onStatusFilterClick(action.selectedStatus)

            is ViewAction.ItemSelect -> onItemSelect(action.entry)
            is ViewAction.ItemPlusOneClick -> onItemPlusOneClick(action.entry)

            is ViewAction.ProgressCardClick -> onProgressCardClick(action.entry)
            is ViewAction.ProgressUpdated -> onProgressUpdated(action.entryId, action.newProgress)
            is ViewAction.RatingCardClick -> onRatingCardClick(action.entry)
            is ViewAction.StatusCardClick -> onStatusCardClick(action.entry)

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

    private fun onStatusFilterClick(selectedStatus: EntryStatus) = viewModelScope.launch {
        val newMap = _uiState.value.statusFilters.map {
            if (it.key == selectedStatus) {
                it.key to !it.value
            } else {
                it.key to it.value
            }
        }.toMap()

        librarySettings.updateStatusFilter(newMap)
    }

    private fun onItemSelect(entry: Entry) {
        _uiState.update { state ->
            state.copy(selectedEntry = entry)
        }
    }

    private fun onItemPlusOneClick(entry: Entry) = viewModelScope.launch(Dispatchers.IO) {
        _uiState.update { state ->
            val newEntries = state.entries.map {
                if (it.entryId == entry.entryId) {
                    it.copy(isUpdating = true)
                } else {
                    it
                }
            }

            state.copy(entries = newEntries.toPersistentList())
        }
        libraryRepository.updateEntry(
            entryId = entry.entryId,
            newProgress = entry.progress + 1
        )
    }

    private fun onProgressCardClick(entry: Entry) = viewModelScope.launch {
        _uiState.update { state ->
            state.copy(
                bottomSheet = LibraryBottomSheet.ProgressBottomSheet(
                    entryId = entry.entryId,
                    currentProgress = entry.progress,
                    maxProgress = entry.maxProgress,
                    title = entry.title,
                    type = entry.type
                )
            )
        }
    }

    private fun onProgressUpdated(entryId: Int, newProgress: String?) {
        // if newProgress = null - then it was cancelled, thats fine.
        // if newProgress.toIntOrNull() = null - then it was invalid text entered.

    }

    private fun onRatingCardClick(entry: Entry) {

    }

    private fun onStatusCardClick(entry: Entry) {

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
            type = type,
            title = titles.toChosenLanguage(titleLanguage),
            posterImage = posterImage.toBestImage(imageQuality),
            coverImage = coverImage.toBestImage(imageQuality),
            progressPercent = progressPercent,
            progress = progress,
            maxProgress = totalLength.takeIf { it != 0 },
            progressDisplay = "$progress / $displayTotalLength",
            airingTimeFrame = "$startDate${if (endDate.isNotBlank()) " - $endDate" else ""}",
            seriesStatus = seriesStatus,
            isUpdating = false,
            canUpdate = canIncrementProgress
        )
    }
}
