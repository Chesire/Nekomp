package com.chesire.nekomp.library.datasource.user

import com.chesire.nekomp.core.model.Image

data class User(
    val id: Int,
    val name: String,
    val about: String,
    val avatar: Image,
    val coverImage: Image,
    val isAuthenticated: Boolean = true
)
