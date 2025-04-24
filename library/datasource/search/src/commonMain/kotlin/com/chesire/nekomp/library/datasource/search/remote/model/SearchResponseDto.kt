package com.chesire.nekomp.library.datasource.search.remote.model

import com.chesire.nekomp.library.datasource.kitsumodels.ImagesDto
import com.chesire.nekomp.library.datasource.kitsumodels.TitlesDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponseDto(
    @SerialName("data")
    val data: List<SearchData>
) {

    @Serializable
    data class SearchData(
        @SerialName("id")
        val id: Int,
        @SerialName("type")
        val type: String,
        @SerialName("attributes")
        val attributes: SearchAttributes
    ) {

        @Serializable
        data class SearchAttributes(
            @SerialName("synopsis")
            val synopsis: String,
            @SerialName("titles")
            val titles: TitlesDto,
            @SerialName("canonicalTitle")
            val canonicalTitle: String,
            @SerialName("subtype")
            val subtype: String,
            @SerialName("status")
            val status: String,
            @SerialName("averageRating")
            val averageRating: String?,
            @SerialName("posterImage")
            val posterImage: ImagesDto?,
            @SerialName("coverImage")
            val coverImage: ImagesDto?
        )
    }
}
