package com.chesire.nekomp.feature.library.core

import com.chesire.nekomp.library.datasource.library.LibraryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class RefreshLibraryEntriesUseCase(private val libraryRepository: LibraryRepository) {

    suspend operator fun invoke() {
        // Fire and forget, since the repository flow will emit new values
        withContext(Dispatchers.IO) {
            libraryRepository.retrieve()
        }
    }
}
