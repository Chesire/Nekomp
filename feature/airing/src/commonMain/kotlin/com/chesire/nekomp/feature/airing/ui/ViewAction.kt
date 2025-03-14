package com.chesire.nekomp.feature.airing.ui

sealed interface ViewAction {
    data object ObservedViewEvent : ViewAction
}
