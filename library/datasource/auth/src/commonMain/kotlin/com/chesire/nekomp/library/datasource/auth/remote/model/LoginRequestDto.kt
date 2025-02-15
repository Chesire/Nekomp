package com.chesire.nekomp.library.datasource.auth.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

const val GRANT_TYPE_PASSWORD = "password"

@Serializable
data class LoginRequestDto(
    @SerialName("username")
    val username: String,
    @SerialName("password")
    val password: String,
    @SerialName("grant_type")
    val grantType: String
)
