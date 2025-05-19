package com.chesire.nekomp.library.datasource.favorite.remote.model

import com.chesire.nekomp.library.datasource.kitsumodels.ImagesDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RetrieveFavoriteCharacterResponseDto(
    @SerialName("data")
    val data: List<DataDto>,
    @SerialName("included")
    val included: List<CharacterIncludedDto>?
)

@Serializable
data class CharacterIncludedDto(
    @SerialName("id")
    val id: Int,
    @SerialName("attributes")
    val attributes: CharacterAttributes
) {

    @Serializable
    data class CharacterAttributes(
        @SerialName("canonicalName")
        val canonicalName: String,
        @SerialName("image")
        val image: ImagesDto?
    )
}
