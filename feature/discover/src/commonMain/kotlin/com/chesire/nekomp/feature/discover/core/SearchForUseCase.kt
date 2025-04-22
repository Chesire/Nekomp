package com.chesire.nekomp.feature.discover.core

import com.chesire.nekomp.core.coroutines.Dispatcher
import com.chesire.nekomp.core.network.NetworkError
import com.chesire.nekomp.library.datasource.search.SearchItem
import com.chesire.nekomp.library.datasource.search.SearchService
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.anyErr
import com.github.michaelbull.result.filterErrors
import com.github.michaelbull.result.filterValues
import com.github.michaelbull.result.mapResult
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class SearchForUseCase(
    private val searchService: SearchService,
    private val dispatcher: Dispatcher
) {

    suspend operator fun invoke(title: String): Result<List<SearchItem>, NetworkError> {
        return withContext(dispatcher.io) {
            val results = awaitAll(
                async { searchService.searchForAnime(title) },
                async { searchService.searchForManga(title) }
            )
            if (results.anyErr()) {
                Err(results.filterErrors().first())
            } else {
                results
                    .filterValues()
                    .flatMap { it }
                    .mapResult { Ok(it) }
            }
        }
    }
}
