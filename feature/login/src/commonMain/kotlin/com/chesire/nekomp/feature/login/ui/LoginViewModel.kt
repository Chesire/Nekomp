package com.chesire.nekomp.feature.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekomp.core.resources.NekoRes
import com.chesire.nekomp.feature.login.core.PerformLoginUseCase
import com.chesire.nekomp.library.datasource.auth.AuthFailure
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import nekomp.core.resources.generated.resources.login_error_generic
import nekomp.core.resources.generated.resources.login_error_invalid_credentials
import org.jetbrains.compose.resources.getString

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
        val loginResult = performLogin(state.email, state.password)

        _uiState.update {
            it.copy(
                isPendingLogin = false,
                viewEvent = if (loginResult.isOk) {
                    ViewEvent.LoginSuccessful
                } else {
                    when (loginResult.error) {
                        AuthFailure.BadRequest -> ViewEvent.LoginFailure(
                            getString(NekoRes.string.login_error_invalid_credentials)
                        )

                        AuthFailure.InvalidCredentials -> ViewEvent.LoginFailure(
                            getString(NekoRes.string.login_error_generic)
                        )
                    }
                }
            )
        }
    }

    private fun onObservedViewEvent() {
        _uiState.update {
            it.copy(viewEvent = null)
        }
    }
}
