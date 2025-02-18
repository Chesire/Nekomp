package com.chesire.nekomp.feature.discover.ui

sealed interface ViewAction {
    data object ObservedViewEvent : ViewAction
}
