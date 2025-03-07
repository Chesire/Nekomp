package com.chesire.nekomp.feature.library.data

enum class SortChoice {
    Default,
    Title,
    StartDate,
    EndDate,
    Rating;

    companion object {

        internal val default: SortChoice = Default

        fun fromString(input: String): SortChoice {
            return SortChoice.entries.find { it.name.lowercase() == input.lowercase() } ?: default
        }
    }
}
