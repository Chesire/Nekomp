package com.chesire.nekomp.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ConsumedDataEntity(
    @PrimaryKey
    val type: String,
    val time: Int,
    val media: Int,
    val units: Int,
    val completed: Int
)
