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
    // val otherTitles: Map<String, String?>,
    val subtype: String,
    val posterImage: String,
    val coverImage: String,
    val averageRating: String,
    val ratingRank: Int,
    val popularityRank: Int
)
