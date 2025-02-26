package com.chesire.nekomp.core.model

data class Titles(
    val canonical: String,
    val english: String,
    val romaji: String,
    /**
     * Priority order of Japanese - Korean - Chinese
     */
    val cjk: String
)
