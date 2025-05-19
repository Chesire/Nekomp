package com.chesire.nekomp.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val type: String,
    val title: String,
    val posterImageTiny: String,
    val posterImageSmall: String,
    val posterImageMedium: String,
    val posterImageLarge: String,
    val posterImageOriginal: String,
    val rank: Int
)
