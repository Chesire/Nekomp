package com.chesire.nekomp.feature.login.core

import com.chesire.nekomp.library.datasource.library.LibraryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class RetrieveLibraryUseCase(private val libraryRepository: LibraryRepository) {

    suspend operator fun invoke(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            libraryRepository.retrieve().map { Unit }
        }
    }
}
