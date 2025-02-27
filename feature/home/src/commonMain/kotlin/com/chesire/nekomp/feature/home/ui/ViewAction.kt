package com.chesire.nekomp.feature.home.ui

sealed interface ViewAction {
    data object ObservedViewEvent : ViewAction
}
