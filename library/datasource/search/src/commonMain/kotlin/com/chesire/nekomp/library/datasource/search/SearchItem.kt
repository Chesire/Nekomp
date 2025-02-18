package com.chesire.nekomp.library.datasource.search

data class SearchItem(
    val id: Int,
    val type: String,
    val synopsis: String,
    val canonicalTitle: String,
    // val otherTitles: Map<String, String?>,
    val subtype: String,
    val posterImage: String
)
