package com.chesire.nekomp.library.datasource.trending

import co.touchlab.kermit.Logger
import com.chesire.nekomp.core.model.Type
import com.chesire.nekomp.library.datasource.trending.local.TrendingStorage
import com.chesire.nekomp.library.datasource.trending.remote.TrendingApi
import com.chesire.nekomp.library.datasource.trending.remote.model.TrendingResponseDto
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

// TODO: Add a remote data source that converts the dtos appropriately?
// TODO: Need to periodically update these
class TrendingRepository(
    private val trendingStorage: TrendingStorage,
    private val trendingApi: TrendingApi
) {

    suspend fun getTrendingAnime(): Result<List<TrendingItem>, Unit> {
        Logger.d("TrendingService") { "Getting trending anime" }

        return if (trendingStorage.trendingAnime.firstOrNull().isNullOrEmpty()) {
            trendingApi
                .trendingAnime()
                .map { it.toTrendingItems() }
                .onSuccess { trendingStorage.updateTrending(it) }
                .fold(
                    onSuccess = { Ok(it) },
                    onFailure = { Err(Unit) } // TODO: Add custom error
                )
        } else {
            Ok(trendingStorage.trendingAnime.first())
        }
    }

    suspend fun getTrendingManga(): Result<List<TrendingItem>, Unit> {
        Logger.d("TrendingService") { "Getting trending manga" }

        return if (trendingStorage.trendingManga.firstOrNull().isNullOrEmpty()) {
            trendingApi
                .trendingManga()
                .map { it.toTrendingItems() }
                .onSuccess { trendingStorage.updateTrending(it) }
                .fold(
                    onSuccess = { Ok(it) },
                    onFailure = { Err(Unit) } // TODO: Add custom error
                )
        } else {
            Ok(trendingStorage.trendingManga.first())
        }
    }

    suspend fun getTopRatedAnime(): Result<List<TrendingItem>, Unit> {
        Logger.d("TrendingService") { "Getting top rated anime" }

        return if (trendingStorage.topRatedAnime.firstOrNull().isNullOrEmpty()) {
            trendingApi
                .topRatedAnime()
                .map { it.toTrendingItems() }
                .onSuccess { trendingStorage.updateTopRated(it) }
                .fold(
                    onSuccess = { Ok(it) },
                    onFailure = { Err(Unit) } // TODO: Add custom error
                )
        } else {
            Ok(trendingStorage.topRatedAnime.first())
        }
    }

    suspend fun getTopRatedManga(): Result<List<TrendingItem>, Unit> {
        Logger.d("TrendingService") { "Getting top rated manga" }

        return if (trendingStorage.topRatedManga.firstOrNull().isNullOrEmpty()) {
            trendingApi
                .topRatedManga()
                .map { it.toTrendingItems() }
                .onSuccess { trendingStorage.updateTopRated(it) }
                .fold(
                    onSuccess = { Ok(it) },
                    onFailure = { Err(Unit) } // TODO: Add custom error
                )
        } else {
            Ok(trendingStorage.topRatedManga.first())
        }
    }

    suspend fun getMostPopularAnime(): Result<List<TrendingItem>, Unit> {
        Logger.d("TrendingService") { "Getting most popular anime" }

        return if (trendingStorage.mostPopularAnime.firstOrNull().isNullOrEmpty()) {
            trendingApi
                .mostPopularAnime()
                .map { it.toTrendingItems() }
                .onSuccess { trendingStorage.updateMostPopular(it) }
                .fold(
                    onSuccess = { Ok(it) },
                    onFailure = { Err(Unit) } // TODO: Add custom error
                )
        } else {
            Ok(trendingStorage.mostPopularAnime.first())
        }
    }

    suspend fun getMostPopularManga(): Result<List<TrendingItem>, Unit> {
        Logger.d("TrendingService") { "Getting most popular manga" }

        return if (trendingStorage.mostPopularManga.firstOrNull().isNullOrEmpty()) {
            trendingApi
                .mostPopularManga()
                .map { it.toTrendingItems() }
                .onSuccess { trendingStorage.updateMostPopular(it) }
                .fold(
                    onSuccess = { Ok(it) },
                    onFailure = { Err(Unit) } // TODO: Add custom error
                )
        } else {
            Ok(trendingStorage.mostPopularManga.first())
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
