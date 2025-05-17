package com.chesire.nekomp.library.datasource.stats

enum class ConsumedStatsType {
    Anime,
    Manga;

    companion object {
        private val default: ConsumedStatsType = Anime

        fun fromString(input: String?): ConsumedStatsType {
            return if (input == null) {
                default
            } else {
                ConsumedStatsType
                    .entries
                    .find { it.name.lowercase() == input.lowercase() }
                    ?: default
            }
        }
    }
}
