package com.chesire.nekomp.feature.settings.ui

import com.chesire.nekomp.core.preferences.ImageQuality
import com.chesire.nekomp.core.preferences.Theme

sealed interface ViewAction {

    data object ThemeClick : ViewAction
    data class ThemeChosen(val theme: Theme?) : ViewAction

    data object TitleLanguageClick : ViewAction

    data object ImageQualityClick : ViewAction
    data class ImageQualityChosen(val imageQuality: ImageQuality?) : ViewAction

    data object RateChanged : ViewAction

    data object LogoutClick : ViewAction

    data object ObservedViewEvent : ViewAction
}
