package com.chesire.nekomp.feature.discover.core

import com.chesire.nekomp.library.datasource.library.LibraryRepository
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class AddItemToTrackingUseCase(private val libraryRepository: LibraryRepository) {

    suspend operator fun invoke(type: String, id: Int): Result<Unit, Unit> {
        return withContext(Dispatchers.IO) {
            val entryType = EntryType.valueOf(type)
            when (entryType) {
                EntryType.Anime -> libraryRepository.addAnime(id)
                EntryType.Manga -> libraryRepository.addManga(id)
            }.map { Unit }
        }
    }
}

// TODO: Put this somewhere in core as it will be used alot
enum class EntryType {
    Anime,
    Manga
}
