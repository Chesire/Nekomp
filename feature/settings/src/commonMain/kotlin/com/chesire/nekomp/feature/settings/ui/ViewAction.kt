package com.chesire.nekomp.feature.settings.ui

sealed interface ViewAction {
    data object ThemeClick : ViewAction
    data object TitleLanguageClick : ViewAction
    data object ImageQualityClick : ViewAction
    data object RateChanged : ViewAction
    data object LogoutClick : ViewAction
    data object ObservedViewEvent : ViewAction
}
