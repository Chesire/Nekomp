package com.chesire.nekomp.feature.discover.core

import com.chesire.nekomp.library.datasource.library.LibraryRepository
import com.github.michaelbull.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class DeleteItemUseCase(private val libraryRepository: LibraryRepository) {

    suspend operator fun invoke(entryId: Int): Result<Unit, Unit> {
        return withContext(Dispatchers.IO) {
            libraryRepository.deleteEntry(entryId)
        }
    }
}
