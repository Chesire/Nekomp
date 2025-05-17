package com.chesire.nekomp.library.datasource.favorite

enum class FavoriteType {
    Character,
    Anime,
    Manga;

    companion object {

        fun fromString(input: String?): FavoriteType? {
            return if (input == null) {
                null
            } else {
                FavoriteType.entries.find { it.name.lowercase() == input.lowercase() }
            }
        }
    }
}
