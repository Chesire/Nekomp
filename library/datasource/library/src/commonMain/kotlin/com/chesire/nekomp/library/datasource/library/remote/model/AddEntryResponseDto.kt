package com.chesire.nekomp.library.datasource.library.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddEntryResponseDto(
    @SerialName("data")
    val data: DataDto,
    @SerialName("included")
    val included: List<IncludedDto>
)
