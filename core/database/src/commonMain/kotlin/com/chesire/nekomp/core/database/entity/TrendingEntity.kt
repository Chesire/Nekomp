package com.chesire.nekomp.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TrendingEntity(
    @PrimaryKey
    val id: Int,
    val type: String, // Enum
    val synopsis: String,
    val canonicalTitle: String,
    val englishTitle: String,
    val romajiTitle: String,
    val cjkTitle: String,
    val subtype: String,
    val status: String,
    val posterImageTiny: String,
    val posterImageSmall: String,
    val posterImageMedium: String,
    val posterImageLarge: String,
    val posterImageOriginal: String,
    val coverImageTiny: String,
    val coverImageSmall: String,
    val coverImageMedium: String,
    val coverImageLarge: String,
    val coverImageOriginal: String,
    val averageRating: String,
    val ratingRank: Int,
    val popularityRank: Int,
    val trendingRank: Int? = null
)
