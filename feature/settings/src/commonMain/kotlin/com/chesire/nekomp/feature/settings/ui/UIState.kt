package com.chesire.nekomp.feature.settings.ui

import com.chesire.nekomp.core.preferences.ImageQuality
import com.chesire.nekomp.core.preferences.Theme
import kotlinx.collections.immutable.ImmutableList

data class UIState(
    val currentTheme: String = "",
    val titleLanguage: String = "",
    val imageQuality: String = "",
    val rateCheckbox: Boolean = false,
    val version: String = "",
    val viewEvent: ViewEvent? = null,
    val bottomSheet: SettingsBottomSheet? = null
)

sealed interface SettingsBottomSheet {

    data class ThemeBottomSheet(
        val themes: ImmutableList<Theme>,
        val selectedTheme: Theme
    ) : SettingsBottomSheet

    data class ImageQualityBottomSheet(
        val qualities: ImmutableList<ImageQuality>,
        val selectedQuality: ImageQuality
    ) : SettingsBottomSheet
}
