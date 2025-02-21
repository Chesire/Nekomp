package com.chesire.nekomp.feature.discover.core

import com.chesire.nekomp.library.datasource.trending.TrendingItem
import com.chesire.nekomp.library.datasource.trending.TrendingRepository
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

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
        return TrendingData(
            trendingAnime = trendingRepository.getTrendingAnime().toPersistentList(),
            trendingManga = trendingRepository.getTrendingManga().toPersistentList(),
            mostPopularAnime = trendingRepository.getMostPopularAnime().toPersistentList(),
            mostPopularManga = trendingRepository.getMostPopularManga().toPersistentList(),
            topRatedAnime = trendingRepository.getTopRatedAnime().toPersistentList(),
            topRatedManga = trendingRepository.getTopRatedManga().toPersistentList()
        )
    }
}
