package com.chesire.nekomp.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LibraryEntryEntity(
    @PrimaryKey
    val id: Int,
    val entryId: Int,
    val type: String, // Enum
    val updatedAt: String,
    val primaryType: String, // Enum
    val subtype: String, // Enum
    val slug: String,
    val canonicalTitle: String,
    val englishTitle: String,
    val romajiTitle: String,
    val cjkTitle: String,
    val seriesStatus: String, // Enum
    val entryStatus: String, // Enum
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
