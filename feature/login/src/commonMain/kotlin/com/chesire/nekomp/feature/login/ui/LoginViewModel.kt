package com.chesire.nekomp.feature.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekomp.feature.login.core.PerformLoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

class LoginViewModel(private val performLogin: PerformLoginUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    fun execute(action: ViewAction) {
        when (action) {
            is ViewAction.EmailUpdated -> onEmailUpdated(action.newEmail)
            is ViewAction.PasswordUpdated -> onPasswordUpdated(action.newPassword)
            ViewAction.LoginPressed -> onLoginPressed()
            ViewAction.ObservedViewEvent -> onObservedViewEvent()
        }
    }

    private fun onEmailUpdated(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    private fun onPasswordUpdated(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
    }

    private fun onLoginPressed() = viewModelScope.launch {
        val state = _uiState.updateAndGet { it.copy(isPendingLogin = true) }
        val result = performLogin(state.email, state.password)
        _uiState.update {
            it.copy(
                isPendingLogin = false,
                viewEvent = if (result) ViewEvent.LoginSuccessful else ViewEvent.LoginFailure("Failure")
            )
        }
    }

    private fun onObservedViewEvent() {
        _uiState.update {
            it.copy(viewEvent = null)
        }
    }
}
