package com.chesire.nekomp.library.datasource.library

data class LibraryEntry(
    val id: Int,
    val userId: Int,
    val type: String, // Enum
    val subtype: String, // Enum
    val slug: String,
    val title: String,
    // val otherTitles: Map<String, String>,
    val seriesStatus: String, // Enum
    val userSeriesStatus: String, // Enum
    val progress: Int,
    val totalLength: Int,
    val rating: Int,
    val posterImage: String,
    val startDate: String,
    val endDate: String
)
