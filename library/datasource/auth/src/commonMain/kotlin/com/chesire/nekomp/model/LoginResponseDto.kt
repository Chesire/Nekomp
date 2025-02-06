package com.chesire.nekomp.model

data class LoginResponseDto(
    val accessToken: String,
    val refreshToken: String
)
