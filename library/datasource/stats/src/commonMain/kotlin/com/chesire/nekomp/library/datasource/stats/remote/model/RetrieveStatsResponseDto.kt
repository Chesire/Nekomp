package com.chesire.nekomp.library.datasource.stats.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RetrieveStatsResponseDto(
    @SerialName("data")
    val data: List<DataDto>
)

@Serializable
data class DataDto(
    @SerialName("id")
    val id: String,
    @SerialName("attributes")
    val attributes: AttributesDto
)

@Serializable
data class AttributesDto(
    @SerialName("statsData")
    val statsData: StatsDataDto
)

@Serializable
data class StatsDataDto(
    @SerialName("time")
    val time: Int,
    @SerialName("media")
    val media: Int,
    @SerialName("units")
    val units: Int,
    @SerialName("completed")
    val completed: Int
)
