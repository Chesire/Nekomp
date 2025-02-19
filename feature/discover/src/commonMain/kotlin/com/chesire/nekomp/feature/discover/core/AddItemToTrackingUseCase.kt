package com.chesire.nekomp.feature.discover.core

import com.chesire.nekomp.core.model.Type
import com.chesire.nekomp.library.datasource.library.LibraryRepository
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class AddItemToTrackingUseCase(private val libraryRepository: LibraryRepository) {

    suspend operator fun invoke(type: Type, id: Int): Result<Unit, Unit> {
        return withContext(Dispatchers.IO) {
            when (type) {
                Type.Anime -> libraryRepository.addAnime(id)
                Type.Manga -> libraryRepository.addManga(id)
            }.map { Unit }
        }
    }
}
