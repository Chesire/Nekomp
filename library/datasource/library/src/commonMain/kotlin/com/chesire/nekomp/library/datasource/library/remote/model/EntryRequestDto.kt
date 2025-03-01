package com.chesire.nekomp.library.datasource.library.remote.model

import com.chesire.nekomp.core.model.Type
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class EntryRequestDto(
    @SerialName("data")
    val data: EntryDataRequestDto
) {

    companion object {

        fun buildAdd(type: Type, seriesId: Int, userId: Int): EntryRequestDto {
            val typeRequestDto = EntryRelationshipTypeRequestDto(
                data = EntryRelationshipDataRequestDto(
                    id = seriesId,
                    type = type.name.lowercase()
                )
            )
            return EntryRequestDto(
                data = EntryDataRequestDto(
                    id = null,
                    type = "libraryEntries",
                    attributes = EntryAttributesRequestDto(
                        progress = 0,
                        status = "current"
                    ),
                    relationships = EntryRelationshipsRequestDto(
                        anime = if (type == Type.Anime) typeRequestDto else null,
                        manga = if (type == Type.Manga) typeRequestDto else null,
                        user = EntryRelationshipTypeRequestDto(
                            data = EntryRelationshipDataRequestDto(
                                id = userId,
                                type = "users"
                            )
                        )
                    )
                )
            )
        }

        fun buildUpdate(entryId: Int, newProgress: Int): EntryRequestDto {
            return EntryRequestDto(
                data = EntryDataRequestDto(
                    id = entryId,
                    type = "libraryEntries",
                    attributes = EntryAttributesRequestDto(
                        progress = newProgress,
                        status = null
                    ),
                    relationships = null
                )
            )
        }
    }
}

@Serializable
data class EntryDataRequestDto(
    @SerialName("id")
    val id: Int?,
    @SerialName("type")
    val type: String,
    @SerialName("attributes")
    val attributes: EntryAttributesRequestDto,
    @SerialName("relationships")
    val relationships: EntryRelationshipsRequestDto?
)

@Serializable
data class EntryAttributesRequestDto(
    @SerialName("progress")
    val progress: Int,
    @SerialName("status")
    val status: String?
)

@Serializable
data class EntryRelationshipsRequestDto(
    @SerialName("anime")
    val anime: EntryRelationshipTypeRequestDto?,
    @SerialName("manga")
    val manga: EntryRelationshipTypeRequestDto?,
    @SerialName("user")
    val user: EntryRelationshipTypeRequestDto?
)

@Serializable
data class EntryRelationshipTypeRequestDto(
    @SerialName("data")
    val data: EntryRelationshipDataRequestDto
)

@Serializable
data class EntryRelationshipDataRequestDto(
    @SerialName("id")
    val id: Int,
    @SerialName("type")
    val type: String
)
