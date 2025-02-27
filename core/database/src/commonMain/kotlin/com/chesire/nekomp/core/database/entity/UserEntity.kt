package com.chesire.nekomp.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val avatarTiny: String,
    val avatarSmall: String,
    val avatarMedium: String,
    val avatarLarge: String,
    val avatarOriginal: String,
    val coverImageTiny: String,
    val coverImageSmall: String,
    val coverImageMedium: String,
    val coverImageLarge: String,
    val coverImageOriginal: String,
)
