package com.chesire.nekomp.library.datasource.trending

import co.touchlab.kermit.Logger
import com.chesire.nekomp.core.model.Type
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
                .trendingManga()
                .map { it.toTrendingItems() }
                .onSuccess { trendingStorage.setTrendingManga(it) }
                .fold(
                    onSuccess = { Ok(it) },
                    onFailure = { Err(Unit) } // TODO: Add custom error
                )
        }
    }

    suspend fun getTopRatedAnime(): Result<List<TrendingItem>, Unit> {
        Logger.d("TrendingService") { "Getting top rated anime" }

        return if (trendingStorage.topRatedAnime.isNotEmpty()) {
            Ok(trendingStorage.topRatedAnime)
        } else {
            trendingApi
                .topRatedAnime()
                .map { it.toTrendingItems() }
                .onSuccess { trendingStorage.setTopRatedAnime(it) }
                .fold(
                    onSuccess = { Ok(it) },
                    onFailure = { Err(Unit) } // TODO: Add custom error
                )
        }
    }

    suspend fun getTopRatedManga(): Result<List<TrendingItem>, Unit> {
        Logger.d("TrendingService") { "Getting top rated manga" }

        return if (trendingStorage.topRatedManga.isNotEmpty()) {
            Ok(trendingStorage.topRatedManga)
        } else {
            trendingApi
                .topRatedManga()
                .map { it.toTrendingItems() }
                .onSuccess { trendingStorage.setTopRatedManga(it) }
                .fold(
                    onSuccess = { Ok(it) },
                    onFailure = { Err(Unit) } // TODO: Add custom error
                )
        }
    }

    suspend fun getMostPopularAnime(): Result<List<TrendingItem>, Unit> {
        Logger.d("TrendingService") { "Getting most popular anime" }

        return if (trendingStorage.mostPopularAnime.isNotEmpty()) {
            Ok(trendingStorage.mostPopularAnime)
        } else {
            trendingApi
                .mostPopularAnime()
                .map { it.toTrendingItems() }
                .onSuccess { trendingStorage.setMostPopularAnime(it) }
                .fold(
                    onSuccess = { Ok(it) },
                    onFailure = { Err(Unit) } // TODO: Add custom error
                )
        }
    }

    suspend fun getMostPopularManga(): Result<List<TrendingItem>, Unit> {
        Logger.d("TrendingService") { "Getting most popular manga" }

        return if (trendingStorage.mostPopularManga.isNotEmpty()) {
            Ok(trendingStorage.mostPopularManga)
        } else {
            trendingApi
                .mostPopularManga()
                .map { it.toTrendingItems() }
                .onSuccess { trendingStorage.setMostPopularManga(it) }
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
                type = Type.fromString(it.type),
                synopsis = it.attributes.synopsis,
                canonicalTitle = it.attributes.canonicalTitle,
                // otherTitles = it.attributes.titles,
                subtype = it.attributes.subtype,
                posterImage = it.attributes.posterImage?.medium ?: "",
                averageRating = it.attributes.averageRating,
                ratingRank = it.attributes.ratingRank,
                popularityRank = it.attributes.popularityRank
            )
        }
    }
}
