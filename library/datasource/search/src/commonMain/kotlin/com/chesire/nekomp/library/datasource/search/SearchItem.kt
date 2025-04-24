package com.chesire.nekomp.library.datasource.search

import com.chesire.nekomp.core.model.Image
import com.chesire.nekomp.core.model.Titles
import com.chesire.nekomp.core.model.Type

data class SearchItem(
    val id: Int,
    val type: Type,
    val synopsis: String,
    val titles: Titles,
    val subtype: String,
    val status: String,
    val totalLength: Int,
    val averageRating: String,
    val posterImage: Image,
    val coverImage: Image
)
