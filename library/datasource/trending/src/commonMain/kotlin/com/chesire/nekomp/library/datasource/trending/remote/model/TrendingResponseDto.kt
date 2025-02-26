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
            val titles: Titles,
            @SerialName("canonicalTitle")
            val canonicalTitle: String,
            @SerialName("subtype")
            val subtype: String,
            @SerialName("posterImage")
            val posterImage: ImageModel?,
            @SerialName("coverImage")
            val coverImage: ImageModel?,
            @SerialName("averageRating")
            val averageRating: String,
            @SerialName("ratingRank")
            val ratingRank: Int,
            @SerialName("popularityRank")
            val popularityRank: Int
        ) {

            @Serializable
            data class Titles(
                @SerialName("english")
                val english: String = "",
                @SerialName("romaji")
                val romaji: String = "",
                @SerialName("japanese")
                val japanese: String = ""
            )

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
                val original: String = "",
            )
        }
    }
}
