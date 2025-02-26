package com.chesire.nekomp.library.datasource.search

import com.chesire.nekomp.core.model.Image
import com.chesire.nekomp.core.model.Title
import com.chesire.nekomp.core.model.Type

data class SearchItem(
    val id: Int,
    val type: Type,
    val synopsis: String,
    val titles: Title,
    val subtype: String,
    val posterImage: Image,
    val coverImage: Image
)
