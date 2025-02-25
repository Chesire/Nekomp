package com.chesire.nekomp.library.datasource.search.remote.model

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
            val titles: Map<String, String?>,
            @SerialName("canonicalTitle")
            val canonicalTitle: String,
            @SerialName("subtype")
            val subtype: String,
            @SerialName("posterImage")
            val posterImage: ImageModel?,
            @SerialName("coverImage")
            val coverImage: ImageModel?
        ) {

            @Serializable
            data class ImageModel(
                @SerialName("tiny")
                val tiny: String = "",
                @SerialName("small")
                val small: String = "",
                @SerialName("medium")
                val medium: String = "",
                @SerialName("large")
                val large: String = "",
                @SerialName("original")
                val original: String = ""
            )
        }
    }
}
