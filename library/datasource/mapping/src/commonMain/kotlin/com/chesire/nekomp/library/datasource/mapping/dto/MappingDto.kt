package com.chesire.nekomp.library.datasource.mapping.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MappingDto(
    @SerialName("mal_id")
    val malId: Int?,
    @SerialName("kitsu_id")
    val kitsuId: Int?,
    @SerialName("anilist_id")
    val aniListId: Int?
)
