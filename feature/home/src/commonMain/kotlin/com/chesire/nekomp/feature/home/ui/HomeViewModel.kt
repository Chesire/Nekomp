package com.chesire.nekomp.feature.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekomp.core.model.Type
import com.chesire.nekomp.library.datasource.library.LibraryRepository
import com.chesire.nekomp.library.datasource.user.UserRepository
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userRepository: UserRepository,
    private val libraryRepository: LibraryRepository
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
                _uiState.update { state ->
                    state.copy(
                        watchList = libraryEntries
                            .filter { it.type == Type.Anime }
                            .filter { it.userSeriesStatus != "complete" }
                            .sortedBy { it.updatedAt }
                            .take(10)
                            .map { libraryEntry ->
                                WatchItem(
                                    name = libraryEntry.titles.canonical, // TODO: get correct
                                    coverImage = libraryEntry.posterImage.medium // TODO: get correct

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
            ViewAction.ObservedViewEvent -> onObservedViewEvent()
        }
    }

    private fun onObservedViewEvent() {
        _uiState.update { state ->
            state.copy(viewEvent = null)
        }
    }
}
