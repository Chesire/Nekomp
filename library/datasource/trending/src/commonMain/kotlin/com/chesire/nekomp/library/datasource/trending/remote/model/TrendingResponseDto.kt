package com.chesire.nekomp.library.datasource.trending.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrendingResponseDto(
    @SerialName("data")
    val data: List<TrendingData>
) {

    @Serializable
    data class TrendingData(
        @SerialName("id")
        val id: Int,
        @SerialName("type")
        val type: String,
        @SerialName("attributes")
        val attributes: TrendingAttributes
    ) {

        @Serializable
        data class TrendingAttributes(
            @SerialName("synopsis")
            val synopsis: String,
            @SerialName("titles")
            val titles: Map<String, String?>,
            @SerialName("canonicalTitle")
            val canonicalTitle: String,
            @SerialName("subtype")
            val subtype: String,
            @SerialName("posterImage")
            val posterImage: ImageModel?
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
                val large: String = ""
            )
        }
    }
}
