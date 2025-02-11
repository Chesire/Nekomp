package com.chesire.nekomp.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LibraryEntryEntity(
    @PrimaryKey
    val id: Int,
    val userId: Int,
    val type: String, // Enum
    val subtype: String, // Enum
    val slug: String,
    val title: String,
    // val otherTitles: Map<String, String>, TODO Just set the EN JA etc titles as fields
    val seriesStatus: String, // Enum
    val userSeriesStatus: String, // Enum
    val progress: Int,
    val totalLength: Int,
    val rating: Int,
    // val posterImage: ImageModel, TODO
    val startDate: String,
    val endDate: String
)
