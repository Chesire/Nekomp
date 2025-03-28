package com.chesire.nekomp.feature.settings.ui

import com.chesire.nekomp.core.preferences.ImageQuality
import com.chesire.nekomp.core.preferences.Theme
import com.chesire.nekomp.core.preferences.TitleLanguage

sealed interface ViewAction {

    data object ThemeClick : ViewAction
    data class ThemeChosen(val theme: Theme?) : ViewAction

    data object TitleLanguageClick : ViewAction
    data class TitleLanguageChosen(val titleLanguage: TitleLanguage?) : ViewAction

    data object ImageQualityClick : ViewAction
    data class ImageQualityChosen(val imageQuality: ImageQuality?) : ViewAction

    data object RateChanged : ViewAction

    data object LogoutClick : ViewAction

    data object ObservedViewEvent : ViewAction
}
