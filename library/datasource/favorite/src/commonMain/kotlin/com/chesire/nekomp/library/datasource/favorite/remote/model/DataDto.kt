package com.chesire.nekomp.library.datasource.favorite.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
        @SerialName("favRank")
        val favRank: Int
    )

    @Serializable
    data class Relationships(
        @SerialName("item")
        val item: RelationshipObject? = null
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
