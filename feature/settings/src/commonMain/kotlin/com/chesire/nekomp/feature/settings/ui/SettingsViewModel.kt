package com.chesire.nekomp.feature.settings.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel() : ViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    init {
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
