package com.chesire.nekomp.feature.library.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekomp.feature.library.core.ObserveLibraryEntriesUseCase
import com.chesire.nekomp.feature.library.core.RefreshLibraryEntriesUseCase
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val refreshLibraryEntries: RefreshLibraryEntriesUseCase,
    private val observeLibraryEntries: ObserveLibraryEntriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            refreshLibraryEntries()
        }
        viewModelScope.launch {
            observeLibraryEntries().collect { entries ->
                _uiState.update { state ->
                    state.copy(
                        entries = entries
                            .map {
                                Entry(
                                    id = it.id,
                                    title = it.title,
                                    image = it.posterImage
                                )
                            }
                            .toImmutableList()
                    )
                }
            }
        }
    }

    fun execute(action: ViewAction) {
        when (action) {
            ViewAction.ObservedViewEvent -> onObservedViewEvent()
        }
    }

    private fun onObservedViewEvent() {
        _uiState.update {
            it.copy(viewEvent = null)
        }
    }
}
