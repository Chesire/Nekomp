package com.chesire.nekomp.library.datasource.library.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class EntryRequestDto(
    @SerialName("data")
    val data: EntryDataRequestDto
) {

    companion object {

        fun build(entryId: Int, newProgress: Int): EntryRequestDto {
            return EntryRequestDto(
                data = EntryDataRequestDto(
                    id = entryId,
                    type = "libraryEntries",
                    attributes = EntryAttributesRequestDto(
                        progress = newProgress
                    )
                )
            )
        }
    }
}

@Serializable
data class EntryDataRequestDto(
    @SerialName("id")
    val id: Int,
    @SerialName("type")
    val type: String,
    @SerialName("attributes")
    val attributes: EntryAttributesRequestDto
)

@Serializable
data class EntryAttributesRequestDto(
    @SerialName("progress")
    val progress: Int
)
