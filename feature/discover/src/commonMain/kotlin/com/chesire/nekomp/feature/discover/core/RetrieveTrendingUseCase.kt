package com.chesire.nekomp.feature.discover.core

import com.chesire.nekomp.library.datasource.trending.TrendingItem
import com.chesire.nekomp.library.datasource.trending.TrendingRepository
import com.github.michaelbull.result.mapBoth

class RetrieveTrendingUseCase(private val trendingRepository: TrendingRepository) {

    suspend fun anime(): List<TrendingItem> {
        return trendingRepository
            .getMostPopularAnime()
            .mapBoth(
                success = { it },
                failure = { emptyList<TrendingItem>() }
            )
    }

    suspend fun manga(): List<TrendingItem> {
        return trendingRepository
            .getTrendingManga()
            .mapBoth(
                success = { it },
                failure = { emptyList<TrendingItem>() }
            )
    }
}
