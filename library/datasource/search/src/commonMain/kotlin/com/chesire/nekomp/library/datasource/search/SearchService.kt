package com.chesire.nekomp.library.datasource.search

import co.touchlab.kermit.Logger
import com.chesire.nekomp.core.model.Type
import com.chesire.nekomp.core.network.NetworkError
import com.chesire.nekomp.library.datasource.kitsumodels.toImage
import com.chesire.nekomp.library.datasource.kitsumodels.toTitles
import com.chesire.nekomp.library.datasource.search.remote.SearchApi
import com.chesire.nekomp.library.datasource.search.remote.model.SearchResponseDto
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.map

// TODO: Add a remote data source that converts the dtos appropriately?
class SearchService(private val searchApi: SearchApi) {

    suspend fun searchForAnime(title: String): Result<List<SearchItem>, NetworkError> {
        Logger.d("SearchService") { "Searching for anime $title" }
        return searchApi.searchForAnime(title).map { it.toSearchItems() }
    }

    suspend fun searchForManga(title: String): Result<List<SearchItem>, NetworkError> {
        Logger.d("SearchService") { "Searching for manga $title" }
        return searchApi.searchForManga(title).map { it.toSearchItems() }
    }

    private fun SearchResponseDto.toSearchItems(): List<SearchItem> {
        return data.map {
            SearchItem(
                id = it.id,
                type = Type.fromString(it.type),
                synopsis = it.attributes.synopsis,
                titles = it.attributes.titles.toTitles(it.attributes.canonicalTitle),
                subtype = it.attributes.subtype,
                status = it.attributes.status,
                totalLength = it.attributes.episodeCount ?: it.attributes.chapterCount ?: -1,
                averageRating = it.attributes.averageRating ?: "0",
                posterImage = it.attributes.posterImage.toImage(),
                coverImage = it.attributes.coverImage.toImage()
            )
        }
    }
}
