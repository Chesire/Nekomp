package com.chesire.nekomp.feature.settings.ui

sealed interface ViewAction {
    data object ObservedViewEvent : ViewAction
}
