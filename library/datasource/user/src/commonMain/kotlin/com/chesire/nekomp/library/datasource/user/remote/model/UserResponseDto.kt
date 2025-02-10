package com.chesire.nekomp.library.datasource.user.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponseDto(
    @SerialName("data")
    val data: List<UserItemDto>
)

@Serializable
data class UserItemDto(
    @SerialName("id")
    val id: Int,
    @SerialName("attributes")
    val attributes: Attributes
) {

    @Serializable
    data class Attributes(
        @SerialName("name")
        val name: String
    )
}
