package com.chesire.nekomp.library.datasource.user

data class User(
    val id: Int,
    val name: String,
    val isAuthenticated: Boolean = true
)
