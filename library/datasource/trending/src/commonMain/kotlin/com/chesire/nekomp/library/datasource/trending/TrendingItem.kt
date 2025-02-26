package com.chesire.nekomp.library.datasource.trending

import com.chesire.nekomp.core.model.Image
import com.chesire.nekomp.core.model.Title
import com.chesire.nekomp.core.model.Type

data class TrendingItem(
    val id: Int,
    val type: Type,
    val synopsis: String,
    val canonicalTitle: String,
    val titles: Title,
    val subtype: String,
    val posterImage: Image,
    val coverImage: Image,
    val averageRating: String,
    val ratingRank: Int,
    val popularityRank: Int
)
