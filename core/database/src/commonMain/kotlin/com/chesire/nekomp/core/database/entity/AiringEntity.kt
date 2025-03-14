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
    val cjkTitle: String
)
