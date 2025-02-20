package com.chesire.nekomp.library.datasource.trending

import com.chesire.nekomp.core.model.Type

data class TrendingItem(
    val id: Int,
    val type: Type,
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
