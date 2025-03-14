package com.chesire.nekomp.library.datasource.airing.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImagesDto(
    @SerialName("jpg")
    val jpg: ImagesDataDto?,
    @SerialName("webp")
    val webp: ImagesDataDto?
) {

    @Serializable
    data class ImagesDataDto(
        @SerialName("image_url")
        val defaultImage: String?,
        @SerialName("large_image_url")
        val largeImage: String?
    )
}
