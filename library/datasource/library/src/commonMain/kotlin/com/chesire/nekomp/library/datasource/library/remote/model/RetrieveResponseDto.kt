package com.chesire.nekomp.library.datasource.library.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RetrieveResponseDto(
    @SerialName("data")
    val data: List<DataDto>,
    @SerialName("included")
    val included: List<IncludedDto>?,
    @SerialName("links")
    val links: Links
)

@Serializable
data class DataDto(
    @SerialName("id")
    val id: Int,
    @SerialName("attributes")
    val attributes: Attributes,
    @SerialName("relationships")
    val relationships: Relationships
) {

    @Serializable
    data class Attributes(
        @SerialName("status")
        val status: String, // enum
        @SerialName("progress")
        val progress: Int,
        @SerialName("ratingTwenty")
        val rating: Int?,
        @SerialName("startedAt")
        val startedAt: String?,
        @SerialName("finishedAt")
        val finishedAt: String?
    )

    @Serializable
    data class Relationships(
        @SerialName("anime")
        val anime: RelationshipObject? = null,
        @SerialName("manga")
        val manga: RelationshipObject? = null
    ) {

        @Serializable
        data class RelationshipObject(
            @SerialName("data")
            val data: RelationshipData? = null
        ) {

            @Serializable
            data class RelationshipData(
                @SerialName("id")
                val id: Int
            )
        }
    }
}

@Serializable
data class IncludedDto(
    @SerialName("id")
    val id: Int,
    @SerialName("type")
    val type: String, // enum
    @SerialName("attributes")
    val attributes: Attributes
) {

    @Serializable
    data class Attributes(
        @SerialName("slug")
        val slug: String,
        @SerialName("canonicalTitle")
        val canonicalTitle: String,
        @SerialName("titles")
        val titles: Map<String, String?>,
        @SerialName("startDate")
        val startDate: String?,
        @SerialName("endDate")
        val endDate: String?,
        @SerialName("subtype")
        val subtype: String, // enum
        @SerialName("status")
        val status: String, // enum
        @SerialName("posterImage")
        val posterImage: ImageModel?,
        @SerialName("chapterCount")
        val chapterCount: Int?,
        @SerialName("episodeCount")
        val episodeCount: Int?
    )
}

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

@Serializable
data class Links(
    @SerialName("first")
    val first: String = "",
    @SerialName("next")
    val next: String = "",
    @SerialName("last")
    val last: String = ""
)
