package com.chesire.nekomp.core.preferences.models

enum class TitleLanguage {
    Canonical,
    English,
    Romaji,
    CJK;

    companion object {

        internal val default: TitleLanguage = Canonical

        fun fromString(input: String): TitleLanguage {
            return TitleLanguage
                .entries
                .find { it.name.lowercase() == input.lowercase() }
                ?: default
        }
    }
}
