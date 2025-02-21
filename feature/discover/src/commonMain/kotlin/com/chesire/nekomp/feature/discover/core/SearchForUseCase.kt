package com.chesire.nekomp.feature.discover.core

import com.chesire.nekomp.library.datasource.search.SearchItem
import com.chesire.nekomp.library.datasource.search.SearchService
import com.github.michaelbull.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class SearchForUseCase(private val searchService: SearchService) {

    suspend operator fun invoke(title: String): Result<List<SearchItem>, Unit> {
        // TODO: Need to choose anime and manga
        return withContext(Dispatchers.IO) { searchService.searchForAnime(title) }
    }
}
