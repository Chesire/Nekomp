package com.chesire.nekomp.library.datasource.kitsumodels

import com.chesire.nekomp.core.model.Titles
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TitlesDto(
    @SerialName("en")
    val english: String?,
    @SerialName("en_us") // Backup for EN
    val englishUS: String?,
    @SerialName("en_jp")
    val englishJP: String?,
    @SerialName("ja_jp")
    val japanese: String?,
    @SerialName("ko_kr")
    val korean: String?,
    @SerialName("zh_cn")
    val chinese: String?
)

fun TitlesDto?.toTitles(canonical: String): Titles {
    return if (this == null) {
        Titles(
            canonical = canonical,
            english = "",
            romaji = "",
            cjk = ""
        )
    } else {
        Titles(
            canonical = canonical,
            english = english ?: englishUS ?: "",
            romaji = englishJP ?: "",
            cjk = japanese ?: korean ?: chinese ?: ""
        )
    }
}
