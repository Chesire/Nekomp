package com.chesire.nekomp.library.datasource.trending

import com.chesire.nekomp.core.model.Image
import com.chesire.nekomp.core.model.Titles
import com.chesire.nekomp.core.model.Type

data class TrendingItem(
    val id: Int,
    val type: Type,
    val synopsis: String,
    val titles: Titles,
    val subtype: String,
    val status: String,
    val posterImage: Image,
    val coverImage: Image,
    val totalLength: Int,
    val averageRating: String,
    val ratingRank: Int,
    val popularityRank: Int,
    val trendingRank: Int
)
