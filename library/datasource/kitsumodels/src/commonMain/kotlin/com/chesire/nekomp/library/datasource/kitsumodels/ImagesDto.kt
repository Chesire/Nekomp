package com.chesire.nekomp.library.datasource.kitsumodels

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImagesDto(
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
