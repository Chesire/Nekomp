package com.chesire.nekomp.core.model

enum class Season {
    Winter,
    Spring,
    Summer,
    Autumn;

    companion object {

        internal val default: Season = Winter // If no other value... maybe throw error?

        fun fromString(input: String?): Season {
            return if (input == null) {
                default
            } else {
                Season.entries.find { it.name.lowercase() == input.lowercase() } ?: default
            }
        }
    }
}
