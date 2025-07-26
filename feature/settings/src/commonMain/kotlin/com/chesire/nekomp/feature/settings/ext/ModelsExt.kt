package com.chesire.nekomp.feature.settings.ext

import com.chesire.nekomp.core.preferences.ImageQuality
import com.chesire.nekomp.core.preferences.Theme
import com.chesire.nekomp.core.preferences.TitleLanguage
import com.chesire.nekomp.core.resources.NekoRes
import nekomp.core.resources.generated.resources.settings_image_quality_high
import nekomp.core.resources.generated.resources.settings_image_quality_highest
import nekomp.core.resources.generated.resources.settings_image_quality_low
import nekomp.core.resources.generated.resources.settings_image_quality_lowest
import nekomp.core.resources.generated.resources.settings_image_quality_medium
import nekomp.core.resources.generated.resources.settings_theme_dark
import nekomp.core.resources.generated.resources.settings_theme_light
import nekomp.core.resources.generated.resources.settings_theme_system
import nekomp.core.resources.generated.resources.settings_title_language_canonical
import nekomp.core.resources.generated.resources.settings_title_language_cjk
import nekomp.core.resources.generated.resources.settings_title_language_english
import nekomp.core.resources.generated.resources.settings_title_language_romaji
import org.jetbrains.compose.resources.StringResource

val Theme.displayString: StringResource
    get() {
        return when (this) {
            Theme.System -> NekoRes.string.settings_theme_system
            Theme.Light -> NekoRes.string.settings_theme_light
            Theme.Dark -> NekoRes.string.settings_theme_dark
        }
    }

val ImageQuality.displayString: StringResource
    get() {
        return when (this) {
            ImageQuality.Lowest -> NekoRes.string.settings_image_quality_lowest
            ImageQuality.Low -> NekoRes.string.settings_image_quality_low
            ImageQuality.Medium -> NekoRes.string.settings_image_quality_medium
            ImageQuality.High -> NekoRes.string.settings_image_quality_high
            ImageQuality.Highest -> NekoRes.string.settings_image_quality_highest
        }
    }

val TitleLanguage.displayString: StringResource
    get() {
        return when (this) {
            TitleLanguage.Canonical -> NekoRes.string.settings_title_language_canonical
            TitleLanguage.English -> NekoRes.string.settings_title_language_english
            TitleLanguage.Romaji -> NekoRes.string.settings_title_language_romaji
            TitleLanguage.CJK -> NekoRes.string.settings_title_language_cjk
        }
    }
