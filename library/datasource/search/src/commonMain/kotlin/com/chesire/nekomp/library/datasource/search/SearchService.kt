package com.chesire.nekomp.library.datasource.search

import co.touchlab.kermit.Logger
import com.chesire.nekomp.library.datasource.search.remote.SearchApi
import com.chesire.nekomp.library.datasource.search.remote.model.SearchResponseDto
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result

// TODO: Add a remote data source that converts the dtos appropriately?
class SearchService(private val searchApi: SearchApi) {

    suspend fun searchForAnime(title: String): Result<List<SearchItem>, Unit> {
        Logger.d("SearchService") { "Searching for anime $title" }
        return searchApi.searchForAnime(title)
            .map { it.toSearchItems() }
            .fold(
                onSuccess = { Ok(it) },
                onFailure = { Err(Unit) } // TODO: Add custom error
            )
    }

    suspend fun searchForManga(title: String): Result<List<SearchItem>, Unit> {
        Logger.d("SearchService") { "Searching for manga $title" }
        return searchApi.searchForManga(title)
            .map { it.toSearchItems() }
            .fold(
                onSuccess = { Ok(it) },
                onFailure = { Err(Unit) } // TODO: Add custom error
            )
    }
}

private fun SearchResponseDto.toSearchItems(): List<SearchItem> {
    return data.map {
        SearchItem(
            id = it.id,
            type = it.type,
            synopsis = it.attributes.synopsis,
            canonicalTitle = it.attributes.canonicalTitle,
            // otherTitles = it.attributes.titles,
            subtype = it.attributes.subtype,
            posterImage = it.attributes.posterImage?.medium ?: ""
        )
    }
}
