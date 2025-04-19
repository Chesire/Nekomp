package com.chesire.nekomp.library.datasource.trending

import co.touchlab.kermit.Logger
import com.chesire.nekomp.core.model.Type
import com.chesire.nekomp.library.datasource.kitsumodels.toImage
import com.chesire.nekomp.library.datasource.kitsumodels.toTitles
import com.chesire.nekomp.library.datasource.trending.local.TrendingStorage
import com.chesire.nekomp.library.datasource.trending.remote.TrendingApi
import com.chesire.nekomp.library.datasource.trending.remote.model.TrendingResponseDto
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.get
import com.github.michaelbull.result.map
import com.github.michaelbull.result.mapBoth
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull

private const val TRENDING_LIMIT = 10

// TODO: Add a remote data source that converts the dtos appropriately?
// TODO: Need to periodically update these on desktop/ios somehow
class TrendingRepository(
    private val trendingStorage: TrendingStorage,
    private val trendingApi: TrendingApi
) {

    suspend fun performFullSync(): List<Result<Any, Any>> {
        Logger.d("TrendingService") { "Syncing all data" }
        return coroutineScope {
            val jobs = awaitAll(
                async {
                    trendingApi.trendingAnime().map { dto ->
                        dto.copy(
                            data = dto.data.mapIndexed { index, item ->
                                item.copy(
                                    attributes = item.attributes.copy(trendingRank = index + 1)
                                )
                            }
                        )
                    }
                },
                async {
                    trendingApi.trendingManga().map { dto ->
                        dto.copy(
                            data = dto.data.mapIndexed { index, item ->
                                item.copy(
                                    attributes = item.attributes.copy(trendingRank = index + 1)
                                )
                            }
                        )
                    }
                },
                async { trendingApi.topRatedAnime() },
                async { trendingApi.topRatedManga() },
                async { trendingApi.mostPopularAnime() },
                async { trendingApi.mostPopularManga() }
            )
            jobs
                .map { jobs ->
                    // Map to a list of success/failure
                    jobs.mapBoth(
                        success = { Ok(it) },
                        failure = { Err(Unit) }
                    )
                }
                .apply {
                    // Now iterate over them all and get a single list of items to put into the DB
                    this
                        .mapNotNull { result ->
                            result.get()?.toTrendingItems()
                        }
                        .flatten()
                        .let { trendingItems ->
                            trendingItems
                                .groupBy { it.id }
                                .values
                                .map { it.firstOrNull { it.trendingRank != -1 } ?: it.first() }
                        }
                        .apply {
                            trendingStorage.clearLegacyData()
                            trendingStorage.updateTrending(this)
                        }
                }
        }
    }

    suspend fun getTrendingAnime(): List<TrendingItem> {
        Logger.d("TrendingService") { "Getting trending anime" }
        return trendingStorage
            .trendingAnime
            .firstOrNull()
            ?.filterNot { it.trendingRank == -1 }
            ?.sortedBy { it.trendingRank }
            ?.take(TRENDING_LIMIT)
            ?: emptyList()
    }

    suspend fun getTrendingManga(): List<TrendingItem> {
        Logger.d("TrendingService") { "Getting trending manga" }
        return trendingStorage
            .trendingManga
            .firstOrNull()
            ?.filterNot { it.trendingRank == -1 }
            ?.sortedBy { it.trendingRank }
            ?.take(TRENDING_LIMIT)
            ?: emptyList()
    }

    suspend fun getTopRatedAnime(): List<TrendingItem> {
        Logger.d("TrendingService") { "Getting top rated anime" }
        return trendingStorage
            .trendingAnime
            .firstOrNull()
            ?.sortedBy { it.ratingRank }
            ?.take(TRENDING_LIMIT)
            ?: emptyList()
    }

    suspend fun getTopRatedManga(): List<TrendingItem> {
        Logger.d("TrendingService") { "Getting top rated manga" }
        return trendingStorage
            .trendingManga
            .firstOrNull()
            ?.sortedBy { it.ratingRank }
            ?.take(TRENDING_LIMIT)
            ?: emptyList()
    }

    suspend fun getMostPopularAnime(): List<TrendingItem> {
        Logger.d("TrendingService") { "Getting most popular anime" }
        return trendingStorage
            .trendingAnime
            .firstOrNull()
            ?.sortedBy { it.popularityRank }
            ?.take(TRENDING_LIMIT)
            ?: emptyList()
    }

    suspend fun getMostPopularManga(): List<TrendingItem> {
        Logger.d("TrendingService") { "Getting most popular manga" }
        return trendingStorage
            .trendingManga
            .firstOrNull()
            ?.sortedBy { it.popularityRank }
            ?.take(TRENDING_LIMIT)
            ?: emptyList()
    }

    private fun TrendingResponseDto.toTrendingItems(): List<TrendingItem> {
        return data.map {
            TrendingItem(
                id = it.id,
                type = Type.fromString(it.type),
                synopsis = it.attributes.synopsis,
                titles = it.attributes.titles.toTitles(it.attributes.canonicalTitle),
                subtype = it.attributes.subtype,
                posterImage = it.attributes.posterImage.toImage(),
                coverImage = it.attributes.coverImage.toImage(),
                averageRating = it.attributes.averageRating ?: "",
                ratingRank = it.attributes.ratingRank ?: -1,
                popularityRank = it.attributes.popularityRank ?: -1,
                trendingRank = it.attributes.trendingRank
            )
        }
    }
}
