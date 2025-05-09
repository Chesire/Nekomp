package com.chesire.nekomp.library.datasource.favorite

import com.chesire.nekomp.core.model.Image

data class Favorite(
    val type: FavoriteType,
    val title: String,
    val image: Image,
    val rank: Int
)
