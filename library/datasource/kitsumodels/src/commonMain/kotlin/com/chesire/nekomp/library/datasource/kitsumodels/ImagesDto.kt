package com.chesire.nekomp.library.datasource.kitsumodels

import com.chesire.nekomp.core.model.Image
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

fun ImagesDto?.toImage(): Image {
    return if (this == null) {
        Image(
            tiny = "",
            small = "",
            medium = "",
            large = "",
            original = ""
        )
    } else {
        Image(
            tiny = tiny,
            small = small,
            medium = medium,
            large = large,
            original = original
        )
    }
}
