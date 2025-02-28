package com.chesire.nekomp.library.datasource.trending.remote.model

import com.chesire.nekomp.library.datasource.kitsumodels.ImagesDto
import com.chesire.nekomp.library.datasource.kitsumodels.TitlesDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

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
            val titles: TitlesDto,
            @SerialName("canonicalTitle")
            val canonicalTitle: String,
            @SerialName("subtype")
            val subtype: String,
            @SerialName("posterImage")
            val posterImage: ImagesDto?,
            @SerialName("coverImage")
            val coverImage: ImagesDto?,
            @SerialName("averageRating")
            val averageRating: String,
            @SerialName("ratingRank")
            val ratingRank: Int,
            @SerialName("popularityRank")
            val popularityRank: Int,
            @Transient
            val trendingRank: Int = -1
        )
    }
}
