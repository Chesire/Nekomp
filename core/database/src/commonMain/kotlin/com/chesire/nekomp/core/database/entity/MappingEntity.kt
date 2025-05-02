package com.chesire.nekomp.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MappingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val malId: Int?,
    val kitsuId: Int?,
    val aniListId: Int?
)
