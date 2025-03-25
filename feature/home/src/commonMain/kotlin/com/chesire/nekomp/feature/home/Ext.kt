package com.chesire.nekomp.feature.home

import com.chesire.nekomp.core.model.Image
import com.chesire.nekomp.core.model.Titles
import com.chesire.nekomp.core.preferences.ImageQuality
import com.chesire.nekomp.core.preferences.TitleLanguage

// TODO: Move these with the other implementations somewhere
// TODO: Find way to do this in compose. Listen in the app class to the flows and then recompose
//       when they change? Or set in the composition local?
internal fun Titles.toChosenLanguage(titleLanguage: TitleLanguage): String {
    return when (titleLanguage) {
        TitleLanguage.Canonical -> canonical
        TitleLanguage.English -> english.ifBlank { canonical }
        TitleLanguage.Romaji -> romaji.ifBlank { canonical }
        TitleLanguage.CJK -> cjk.ifBlank { canonical }
    }
}

internal fun Image.toBestImage(imageQuality: ImageQuality): String {
    return when (imageQuality) {
        ImageQuality.Lowest -> lowest
        ImageQuality.Low -> low
        ImageQuality.Medium -> middle
        ImageQuality.High -> high
        ImageQuality.Highest -> highest
    }
}
