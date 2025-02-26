package com.chesire.nekomp.library.datasource.kitsumodels

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
