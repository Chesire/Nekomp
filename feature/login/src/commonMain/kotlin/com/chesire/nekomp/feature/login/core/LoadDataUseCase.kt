package com.chesire.nekomp.feature.login.core

import com.chesire.nekomp.library.datasource.library.LibraryRepository
import com.chesire.nekomp.library.datasource.trending.TrendingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * This loads all the data for a users libraries, trending etc.
 */
class LoadDataUseCase(
    private val libraryRepository: LibraryRepository,
    private val trendingRepository: TrendingRepository,
    private val externalScope: CoroutineScope
) {

    operator fun invoke() {
        externalScope.launch(Dispatchers.IO) {
            async { libraryRepository.retrieve() }
            async { trendingRepository.performFullSync() }
        }
    }
}
