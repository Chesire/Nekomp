package com.chesire.nekomp.library.datasource.library

import co.touchlab.kermit.Logger
import com.chesire.nekomp.library.datasource.library.local.LibraryStorage
import com.chesire.nekomp.library.datasource.library.remote.LibraryApi
import com.chesire.nekomp.library.datasource.library.remote.model.DataDto
import com.chesire.nekomp.library.datasource.library.remote.model.IncludedDto
import com.chesire.nekomp.library.datasource.library.remote.model.RetrieveResponseDto
import com.chesire.nekomp.library.datasource.user.UserRepository
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.anyOk
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlin.Result as KResult

private const val LIMIT = 20

// TODO: Add a remote data source that converts the dtos appropriately?
// TODO: Handle errors and maybe retrying?
class LibraryRepository(
    private val libraryApi: LibraryApi,
    private val libraryStorage: LibraryStorage,
    private val userRepository: UserRepository // TODO: Inject method to get the user id?
) {

    val libraryEntries: Flow<List<LibraryEntry>> = libraryStorage.libraryEntries

    suspend fun retrieve(): Result<Unit, Unit> {
        Logger.d("LibraryRepository") { "Making call to retrieve library entries" }
        val user = userRepository.user.firstOrNull()
        if (user?.isAuthenticated != true) {
            Logger.e("LibraryRepository") { "No user object, cancelling retrieve" }
            return Err(Unit) // TODO: Add custom error type
        }

        val result = coroutineScope {
            awaitAll(
                async { retrieve(user.id, libraryApi::retrieveAnime) },
                async { retrieve(user.id, libraryApi::retrieveManga) }
            )
        }

        return if (result.anyOk()) Ok(Unit) else Err(Unit)
    }

    private suspend fun retrieve(
        userId: Int,
        apiCall: suspend (userId: Int, offset: Int, limit: Int) -> KResult<RetrieveResponseDto>
    ): Result<List<LibraryEntry>, Unit> {
        var isSuccess = false
        val entries = mutableListOf<LibraryEntry>()
        var offset = 0
        var page = 0
        var next = ""
        do {
            apiCall(userId, offset, LIMIT)
                .map { dto ->
                    next = dto.links.next
                    page++
                    offset = LIMIT * page
                    if (dto.included == null) {
                        emptyList()
                    } else {
                        buildEntries(dto)
                    }
                }
                .onSuccess {
                    isSuccess = true
                    entries.addAll(it)
                    libraryStorage.updateEntries(it)
                }
                .onFailure {
                    return@retrieve Err(Unit)
                }
        } while (next.isNotBlank())

        Logger.d("LibraryRepository") {
            "Retrieve call $apiCall, got entries [$isSuccess], total entries ${entries.count()}"
        }
        return if (isSuccess) Ok(entries) else Err(Unit)
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

    suspend fun addAnime(entryId: Int): Result<LibraryEntry, Unit> {
        Logger.d("LibraryRepository") { "Making call to add anime $entryId" }
        val user = userRepository.user.firstOrNull()
        if (user?.isAuthenticated != true) {
            Logger.e("LibraryRepository") { "No user object, cancelling add call" }
            return Err(Unit) // TODO: Add custom error type
        }

        return addEntry(user.id, entryId, EntryType.ANIME)
    }

    suspend fun addManga(entryId: Int): Result<LibraryEntry, Unit> {
        Logger.d("LibraryRepository") { "Making call to add manga $entryId" }
        val user = userRepository.user.firstOrNull()
        if (user?.isAuthenticated != true) {
            Logger.e("LibraryRepository") { "No user object, cancelling add call" }
            return Err(Unit) // TODO: Add custom error type
        }

        return addEntry(user.id, entryId, EntryType.MANGA)
    }

    private suspend fun addEntry(
        userId: Int,
        entryId: Int,
        type: EntryType
    ): Result<LibraryEntry, Unit> {
        val addJson = createAddDto(
            userId,
            entryId,
            type.value
        )

        return when (type) {
            EntryType.ANIME -> libraryApi.addAnime(addJson)
            EntryType.MANGA -> libraryApi.addManga(addJson)
        }
            .map {
                val id = it.data.relationships.anime?.data?.id
                    ?: it.data.relationships.manga?.data?.id
                it.included
                    .find { it.id == id }
                    ?.let { included ->
                        buildEntry(included, it.data)
                    }
                    ?: return@addEntry Err(Unit)
            }
            .onSuccess {
                libraryStorage.updateEntry(it)
            }
            .fold(
                onSuccess = { Ok(it) },
                onFailure = { Err(Unit) }
            )
    }
}

// TODO: This could probably be some actual DTO classes?
private fun createAddDto(
    userId: Int,
    seriesId: Int,
    seriesType: String
) =
    """
{
  "data": {
    "type": "libraryEntries",
    "attributes": {
      "progress": 0,
      "status": "current"
    },
    "relationships": {
      "$seriesType": {
        "data": {
          "type": "$seriesType",
          "id": $seriesId
        }
      },
      "user": {
        "data": {
          "type": "users",
          "id": $userId
        }
      }
    }
  }
}
        """.trimIndent()

private enum class EntryType(val value: String) {
    ANIME("anime"),
    MANGA("manga")
}
