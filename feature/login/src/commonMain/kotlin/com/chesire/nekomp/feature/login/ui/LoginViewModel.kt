package com.chesire.nekomp.feature.login.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    fun execute(action: ViewAction) {
        when (action) {
            is ViewAction.EmailUpdated -> onEmailUpdated(action.newEmail)
            is ViewAction.PasswordUpdated -> onPasswordUpdated(action.newPassword)
            ViewAction.LoginPressed -> onLoginPressed()
        }
    }

    private fun onEmailUpdated(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    private fun onPasswordUpdated(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
    }

    private fun onLoginPressed() {
        // execute login call
    }
}
