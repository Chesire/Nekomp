package com.chesire.nekomp.feature.settings.ui

sealed interface ViewEvent {
    data object LoggedOut : ViewEvent
}
