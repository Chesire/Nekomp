package com.chesire.nekomp.library.datasource.search

import com.chesire.nekomp.core.model.Type

data class SearchItem(
    val id: Int,
    val type: Type,
    val synopsis: String,
    val canonicalTitle: String,
    // val otherTitles: Map<String, String?>,
    val subtype: String,
    val posterImage: String,
    val coverImage: String
)
