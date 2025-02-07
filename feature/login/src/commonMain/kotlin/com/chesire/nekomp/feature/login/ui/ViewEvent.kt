package com.chesire.nekomp.feature.login.ui

sealed interface ViewEvent {
    data object LoginSuccessful : ViewEvent
    data class LoginFailure(val errorMessage: String) : ViewEvent
}
