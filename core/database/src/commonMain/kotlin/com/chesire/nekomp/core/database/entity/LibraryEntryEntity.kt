package com.chesire.nekomp.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LibraryEntryEntity(
    @PrimaryKey
    val id: Int,
    val userId: Int,
    val type: String, // Enum
    val primaryType: String, // Enum
    val subtype: String, // Enum
    val slug: String,
    val title: String,
    val englishTitle: String,
    val romajiTitle: String,
    val japaneseTitle: String,
    val seriesStatus: String, // Enum
    val userSeriesStatus: String, // Enum
    val progress: Int,
    val totalLength: Int,
    val rating: Int,
    val posterImageTiny: String,
    val posterImageSmall: String,
    val posterImageMedium: String,
    val posterImageLarge: String,
    val posterImageOriginal: String,
    val startDate: String,
    val endDate: String
)
