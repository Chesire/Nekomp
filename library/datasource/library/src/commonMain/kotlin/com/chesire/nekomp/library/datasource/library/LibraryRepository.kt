package com.chesire.nekomp.library.datasource.library

import com.chesire.nekomp.library.datasource.library.local.LibraryStorage
import com.chesire.nekomp.library.datasource.library.remote.LibraryApi
import com.chesire.nekomp.library.datasource.library.remote.model.DataDto
import com.chesire.nekomp.library.datasource.library.remote.model.IncludedDto
import com.chesire.nekomp.library.datasource.library.remote.model.RetrieveResponseDto
import com.chesire.nekomp.library.datasource.user.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

private const val LIMIT = 20

class LibraryRepository(
    private val libraryApi: LibraryApi,
    private val libraryStorage: LibraryStorage,
    private val userRepository: UserRepository
) {

    val libraryEntries: Flow<List<LibraryEntry>> = libraryStorage.libraryEntries

    suspend fun retrieve(): Result<List<LibraryEntry>> {
        // TODO: Add a remote data source that converts the dtos appropriately
        // For now retrieve 20 anime, it should cycle through getting all of them using pagination
        // it should also pull all the manga in the call
        val user = userRepository.user.first()
        if (!user.isAuthenticated) {
            return Result.failure(Throwable()) // TODO: Add custom exception for no user
        }
        return libraryApi.retrieveAnime(user.id, 0, LIMIT)
            .map { dto ->
                if (dto.included == null) {
                    emptyList()
                } else {
                    buildEntries(dto)
                }
            }
            .onSuccess {
                libraryStorage.updateEntries(it)
            }
    }

    private fun buildEntries(body: RetrieveResponseDto): List<LibraryEntry> {
        return body.data.mapNotNull { entry ->
            if (body.included == null) {
                null
            } else {
                val id = entry.relationships.anime?.data?.id
                    ?: entry.relationships.manga?.data?.id
                    ?: return@mapNotNull null
                body.included
                    .find { it.id == id }
                    ?.let { included ->
                        buildEntry(included, entry)
                    }
            }
        }
    }

    private fun buildEntry(included: IncludedDto, data: DataDto): LibraryEntry {
        return LibraryEntry(
            id = included.id,
            userId = data.id,
            type = included.type,
            subtype = included.attributes.subtype,
            slug = included.attributes.slug,
            title = included.attributes.canonicalTitle,
            // otherTitles = included.attributes.titles,
            seriesStatus = included.attributes.status,
            userSeriesStatus = data.attributes.status,
            progress = data.attributes.progress,
            totalLength = included.attributes.episodeCount ?: included.attributes.chapterCount ?: 0,
            rating = data.attributes.rating ?: 0,
            posterImage = included.attributes.posterImage?.medium ?: "",
            startDate = included.attributes.startDate ?: "",
            endDate = included.attributes.endDate ?: ""
        )
    }
}
