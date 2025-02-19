package com.chesire.nekomp.feature.discover.core

import com.chesire.nekomp.library.datasource.library.LibraryEntry
import com.chesire.nekomp.library.datasource.library.LibraryRepository
import kotlinx.coroutines.flow.Flow

class RetrieveLibraryUseCase(private val libraryRepository: LibraryRepository) {

    operator fun invoke(): Flow<List<LibraryEntry>> {
        return libraryRepository.libraryEntries
    }
}
