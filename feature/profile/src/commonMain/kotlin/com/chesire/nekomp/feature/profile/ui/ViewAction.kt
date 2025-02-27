package com.chesire.nekomp.feature.profile.ui

sealed interface ViewAction {
    data object ObservedViewEvent : ViewAction
}
