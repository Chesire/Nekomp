package com.chesire.nekomp.core.ext

import com.chesire.nekomp.core.model.Titles
import com.chesire.nekomp.core.preferences.TitleLanguage

/**
 * Gets the best title from [Titles] based on the passed in [titleLanguage].
 */
fun Titles.toChosenLanguage(titleLanguage: TitleLanguage): String {
    return when (titleLanguage) {
        TitleLanguage.Canonical -> canonical
        TitleLanguage.English -> english.ifBlank { canonical }
        TitleLanguage.Romaji -> romaji.ifBlank { canonical }
        TitleLanguage.CJK -> cjk.ifBlank { canonical }
    }
}
