package com.chesire.nekomp.core.model

enum class Type {
    Anime,
    Manga;

    companion object {

        fun fromString(input: String): Type {
            return entries.find { it.name.lowercase() == input.lowercase() } ?: Anime
        }
    }
}
