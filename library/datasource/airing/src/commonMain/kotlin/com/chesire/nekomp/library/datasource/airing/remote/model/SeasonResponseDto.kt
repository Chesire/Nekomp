package com.chesire.nekomp.library.datasource.airing.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeasonResponseDto(
    @SerialName("pagination")
    val pagination: PaginationDto,
    @SerialName("data")
    val data: List<SeasonDataDto>
) {

    @Serializable
    data class SeasonDataDto(
        /**
         * Id of the show on MyAnimeList which Jikan uses for data.
         */
        @SerialName("mal_id")
        val malId: Int,
        @SerialName("url")
        val url: String,
        @SerialName("images")
        val images: ImagesDto?,
        /**
         * In other areas of the app this is known as the canonical title.
         */
        @SerialName("title")
        val title: String,
        @SerialName("title_english")
        val titleEnglish: String?,
        @SerialName("title_japanese")
        val titleJapanese: String?,
        @SerialName("episodes")
        val episodes: Int?,
        @SerialName("status")
        val status: String?,
        @SerialName("airing")
        val airing: Boolean,
        @SerialName("season")
        val season: String?,
        @SerialName("year")
        val year: Int?,
        @SerialName("broadcast")
        val broadcastDto: BroadcastDto?
    ) {

        @Serializable
        data class BroadcastDto(
            @SerialName("day")
            val day: String?,
            @SerialName("time")
            val time: String?,
            @SerialName("timezone")
            val timezone: String?
        )
    }
}
