package com.chesire.nekomp.core.model

data class Title(
    val canonical: String,
    val english: String,
    val romaji: String,
    /**
     * Priority order of Japanese - Korean - Chinese
     */
    val cjk: String
)
