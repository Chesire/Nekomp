package com.chesire.nekomp.feature.login.ui

sealed interface ViewAction {
    data class EmailUpdated(val newEmail: String) : ViewAction
    data class PasswordUpdated(val newPassword: String) : ViewAction
    data object LoginPressed : ViewAction
    data object ObservedViewEvent : ViewAction
}
