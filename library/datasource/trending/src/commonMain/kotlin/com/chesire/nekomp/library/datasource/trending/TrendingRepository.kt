package com.chesire.nekomp.library.datasource.trending

import co.touchlab.kermit.Logger
import com.chesire.nekomp.core.model.Image
import com.chesire.nekomp.core.model.Title
import com.chesire.nekomp.core.model.Type
import com.chesire.nekomp.library.datasource.trending.local.TrendingStorage
import com.chesire.nekomp.library.datasource.trending.remote.TrendingApi
import com.chesire.nekomp.library.datasource.trending.remote.model.TrendingResponseDto
import com.chesire.nekomp.library.datasource.trending.remote.model.TrendingResponseDto.TrendingData.TrendingAttributes.ImageModel
import com.chesire.nekomp.library.datasource.trending.remote.model.TrendingResponseDto.TrendingData.TrendingAttributes.Titles
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.map
import com.github.michaelbull.result.onSuccess
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
            awaitAll(
                async { trendingApi.trendingAnime() }, // TODO: maybe need to set a trending rank manually?
                async { trendingApi.trendingManga() },
                async { trendingApi.topRatedAnime() },
                async { trendingApi.topRatedManga() },
                async { trendingApi.mostPopularAnime() },
                async { trendingApi.mostPopularManga() }
            )
                .map { jobs ->
                    jobs.fold(
                        onSuccess = { Ok(it) },
                        onFailure = { Err(Unit) }
                    )
                }
                .map { results ->
                    results
                        .map { it.toTrendingItems() }
                        .onSuccess { trendingStorage.updateTrending(it) }
                }
        }
    }

    suspend fun getTrendingAnime(): List<TrendingItem> {
        return trendingStorage
            .trendingAnime
            .firstOrNull()
            ?.sortedBy { it.averageRating }
            ?.take(TRENDING_LIMIT)
            ?: emptyList()
    }

    suspend fun getTrendingManga(): List<TrendingItem> {
        Logger.d("TrendingService") { "Getting trending manga" }
        return trendingStorage
            .trendingManga
            .firstOrNull()
            ?.sortedBy { it.averageRating }
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
                canonicalTitle = it.attributes.canonicalTitle,
                titles = it.attributes.titles.toTitle(),
                subtype = it.attributes.subtype,
                posterImage = it.attributes.posterImage.toImage(),
                coverImage = it.attributes.coverImage.toImage(),
                averageRating = it.attributes.averageRating,
                ratingRank = it.attributes.ratingRank,
                popularityRank = it.attributes.popularityRank
            )
        }
    }

    private fun Titles?.toTitle(): Title {
        return if (this == null) {
            Title("", "", "")
        } else {
            Title(
                english = english,
                romaji = romaji,
                japanese = japanese
            )
        }
    }

    private fun ImageModel?.toImage(): Image {
        return if (this == null) {
            Image("", "", "", "", "")
        } else {
            Image(
                tiny = tiny,
                small = small,
                medium = medium,
                large = large,
                original = original
            )
        }
    }
}
