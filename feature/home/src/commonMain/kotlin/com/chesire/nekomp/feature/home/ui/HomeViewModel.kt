package com.chesire.nekomp.feature.home.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel() : ViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

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
