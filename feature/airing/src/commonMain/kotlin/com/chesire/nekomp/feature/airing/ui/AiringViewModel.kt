package com.chesire.nekomp.feature.airing.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.chesire.nekomp.library.datasource.airing.AiringRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AiringViewModel(private val airingRepository: AiringRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            airingRepository.currentAiring.collect {
                Logger.d("AiringViewModel") { "Got new airing list - $it" }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            // Put this somewhere else so we can do it on app start
            airingRepository.syncCurrentAiring()
        }
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
