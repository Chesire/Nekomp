package com.chesire.nekomp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    @SerialName("username")
    val username: String,
    @SerialName("password")
    val password: String,
    @SerialName("grant_type")
    val grantType: String
)
