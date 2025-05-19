package com.chesire.nekomp.library.datasource.favorite.remote.model

import com.chesire.nekomp.library.datasource.kitsumodels.ImagesDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RetrieveFavoriteMediaResponseDto(
    @SerialName("data")
    val data: List<DataDto>,
    @SerialName("included")
    val included: List<MediaIncludedDto>?
)

@Serializable
data class MediaIncludedDto(
    @SerialName("id")
    val id: Int,
    @SerialName("attributes")
    val attributes: MediaAttributes
) {

    @Serializable
    data class MediaAttributes(
        @SerialName("canonicalTitle")
        val canonicalTitle: String,
        @SerialName("posterImage")
        val posterImage: ImagesDto?
    )
}
