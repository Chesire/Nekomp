package com.chesire.nekomp.library.datasource.auth.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

const val GRANT_TYPE_REFRESH = "refresh_token"

@Serializable
data class RefreshRequestDto(
    @SerialName("refresh_token")
    val refreshToken: String,
    @SerialName("grant_type")
    val grantType: String
)
