package com.chesire.nekomp.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AiringEntity(
    @PrimaryKey
    val malId: Int,
    val canonicalTitle: String,
    val englishTitle: String,
    val romajiTitle: String,
    val cjkTitle: String,
    val posterImageTiny: String,
    val posterImageSmall: String,
    val posterImageMedium: String,
    val posterImageLarge: String,
    val posterImageOriginal: String,
    val airing: Boolean,
    val season: String,
    val year: Int,
    val airingDayOfWeek: Int,
    val airingHour: Int,
    val airingMinute: Int,
    val airingTimeZone: String
)
