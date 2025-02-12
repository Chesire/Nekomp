package com.chesire.nekomp.feature.library.ui

sealed interface ViewAction {
    data object ObservedViewEvent : ViewAction
}
