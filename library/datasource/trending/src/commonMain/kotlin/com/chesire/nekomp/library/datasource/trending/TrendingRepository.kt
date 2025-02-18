package com.chesire.nekomp.library.datasource.trending

import co.touchlab.kermit.Logger
import com.chesire.nekomp.library.datasource.trending.local.TrendingStorage
import com.chesire.nekomp.library.datasource.trending.remote.TrendingApi
import com.chesire.nekomp.library.datasource.trending.remote.model.TrendingResponseDto
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result

// TODO: Add a remote data source that converts the dtos appropriately?
class TrendingRepository(
    private val trendingStorage: TrendingStorage,
    private val trendingApi: TrendingApi
) {

    suspend fun getTrendingAnime(): Result<List<TrendingItem>, Unit> {
        Logger.d("TrendingService") { "Getting trending anime" }

        return if (trendingStorage.trendingAnime.isNotEmpty()) {
            Ok(trendingStorage.trendingAnime)
        } else {
            trendingApi
                .trendingAnime()
                .map { it.toTrendingItems() }
                .onSuccess { trendingStorage.setTrendingAnime(it) }
                .fold(
                    onSuccess = { Ok(it) },
                    onFailure = { Err(Unit) } // TODO: Add custom error
                )
        }
    }

    suspend fun getTrendingManga(): Result<List<TrendingItem>, Unit> {
        Logger.d("TrendingService") { "Getting trending manga" }

        return if (trendingStorage.trendingManga.isNotEmpty()) {
            Ok(trendingStorage.trendingManga)
        } else {
            trendingApi
                .trendingAnime()
                .map { it.toTrendingItems() }
                .onSuccess { trendingStorage.setTrendingManga(it) }
                .fold(
                    onSuccess = { Ok(it) },
                    onFailure = { Err(Unit) } // TODO: Add custom error
                )
        }
    }

    private fun TrendingResponseDto.toTrendingItems(): List<TrendingItem> {
        return data.map {
            TrendingItem(
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
}
