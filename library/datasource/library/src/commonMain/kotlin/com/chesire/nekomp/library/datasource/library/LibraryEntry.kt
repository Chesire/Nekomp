package com.chesire.nekomp.library.datasource.library

import com.chesire.nekomp.core.model.Image
import com.chesire.nekomp.core.model.Titles
import com.chesire.nekomp.core.model.Type

data class LibraryEntry(
    val id: Int,
    val userId: Int,
    val type: Type,
    val primaryType: String, // Enum
    val subtype: String, // Enum
    val slug: String,
    val titles: Titles,
    val seriesStatus: String, // Enum
    val userSeriesStatus: String, // Enum
    val progress: Int,
    val totalLength: Int,
    val rating: Int,
    val posterImage: Image,
    val startDate: String,
    val endDate: String
)
