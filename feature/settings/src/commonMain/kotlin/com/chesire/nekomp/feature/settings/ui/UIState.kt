package com.chesire.nekomp.feature.settings.ui

import com.chesire.nekomp.core.preferences.ImageQuality
import com.chesire.nekomp.core.preferences.Theme
import com.chesire.nekomp.core.preferences.TitleLanguage
import kotlinx.collections.immutable.ImmutableList

data class UIState(
    val currentTheme: String = "",
    val titleLanguage: String = "",
    val imageQuality: String = "",
    val rateChecked: Boolean = false,
    val helpUrl: String = "",
    val version: String = "",
    val viewEvent: ViewEvent? = null,
    val bottomSheet: SettingsBottomSheet? = null
)

sealed interface SettingsBottomSheet {

    data class ThemeBottomSheet(
        val themes: ImmutableList<Theme>,
        val selectedTheme: Theme
    ) : SettingsBottomSheet

    data class TitleLanguageBottomSheet(
        val languages: ImmutableList<TitleLanguage>,
        val selectedLanguage: TitleLanguage
    ) : SettingsBottomSheet

    data class ImageQualityBottomSheet(
        val qualities: ImmutableList<ImageQuality>,
        val selectedQuality: ImageQuality
    ) : SettingsBottomSheet
}
