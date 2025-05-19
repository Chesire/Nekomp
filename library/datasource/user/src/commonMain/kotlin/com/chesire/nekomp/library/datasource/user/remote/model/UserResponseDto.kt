package com.chesire.nekomp.library.datasource.user.remote.model

import com.chesire.nekomp.library.datasource.kitsumodels.ImagesDto
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
        val name: String,
        @SerialName("about")
        val about: String,
        @SerialName("avatar")
        val avatar: ImagesDto?,
        @SerialName("coverImage")
        val coverImage: ImagesDto?
    )
}
