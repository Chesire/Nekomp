package com.chesire.nekomp.feature.discover.core

import com.chesire.nekomp.library.datasource.trending.TrendingItem
import com.chesire.nekomp.library.datasource.trending.TrendingRepository
import com.github.michaelbull.result.getOr
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class RetrieveTrendingDataUseCase(private val trendingRepository: TrendingRepository) {

    data class TrendingData(
        val trendingAnime: ImmutableList<TrendingItem>,
        val trendingManga: ImmutableList<TrendingItem>,
        val mostPopularAnime: ImmutableList<TrendingItem>,
        val mostPopularManga: ImmutableList<TrendingItem>,
        val topRatedAnime: ImmutableList<TrendingItem>,
        val topRatedManga: ImmutableList<TrendingItem>
    )

    suspend operator fun invoke(): TrendingData {
        return coroutineScope {
            val jobs = awaitAll(
                async { trendingRepository.getTrendingAnime().getOr(emptyList()) },
                async { trendingRepository.getTrendingManga().getOr(emptyList()) },
                async { trendingRepository.getMostPopularAnime().getOr(emptyList()) },
                async { trendingRepository.getMostPopularManga().getOr(emptyList()) },
                async { trendingRepository.getTopRatedAnime().getOr(emptyList()) },
                async { trendingRepository.getTopRatedManga().getOr(emptyList()) }
            )
            TrendingData(
                trendingAnime = jobs[0].toPersistentList(),
                trendingManga = jobs[1].toPersistentList(),
                mostPopularAnime = jobs[2].toPersistentList(),
                mostPopularManga = jobs[3].toPersistentList(),
                topRatedAnime = jobs[4].toPersistentList(),
                topRatedManga = jobs[5].toPersistentList()
            )
        }
    }
}
