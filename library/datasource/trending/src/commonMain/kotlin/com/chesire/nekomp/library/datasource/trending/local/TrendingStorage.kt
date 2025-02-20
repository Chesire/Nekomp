package com.chesire.nekomp.library.datasource.trending.local

import com.chesire.nekomp.core.database.dao.MostPopularDao
import com.chesire.nekomp.core.database.dao.TopRatedDao
import com.chesire.nekomp.core.database.dao.TrendingDao
import com.chesire.nekomp.core.database.entity.TrendingEntity
import com.chesire.nekomp.core.model.Type
import com.chesire.nekomp.library.datasource.trending.TrendingItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrendingStorage(
    private val mostPopularDao: MostPopularDao,
    private val topRatedDao: TopRatedDao,
    private val trendingDao: TrendingDao
) {

    val mostPopularAnime: Flow<List<TrendingItem>> = mostPopularDao
        .mostPopular()
        .map { items ->
            items
                .filter { Type.fromString(it.type) == Type.Anime }
                .map { it.toTrendingItem() }
        }

    val mostPopularManga: Flow<List<TrendingItem>> = mostPopularDao
        .mostPopular()
        .map { items ->
            items
                .filter { Type.fromString(it.type) == Type.Manga }
                .map { it.toTrendingItem() }
        }

    val topRatedAnime: Flow<List<TrendingItem>> = topRatedDao
        .topRated()
        .map { items ->
            items
                .filter { Type.fromString(it.type) == Type.Anime }
                .map { it.toTrendingItem() }
        }

    val topRatedManga: Flow<List<TrendingItem>> = topRatedDao
        .topRated()
        .map { items ->
            items
                .filter { Type.fromString(it.type) == Type.Manga }
                .map { it.toTrendingItem() }
        }

    val trendingAnime: Flow<List<TrendingItem>> = trendingDao
        .trending()
        .map { items ->
            items
                .filter { Type.fromString(it.type) == Type.Anime }
                .map { it.toTrendingItem() }
        }

    val trendingManga: Flow<List<TrendingItem>> = trendingDao
        .trending()
        .map { items ->
            items
                .filter { Type.fromString(it.type) == Type.Manga }
                .map { it.toTrendingItem() }
        }

    suspend fun updateMostPopular(newMostPopular: List<TrendingItem>) {
        mostPopularDao.apply {
            val models = newMostPopular.map { it.toTrendingEntity() }
            clearType(models.first().type)
            upsert(models)
        }
    }

    suspend fun updateTopRated(newTopRated: List<TrendingItem>) {
        topRatedDao.apply {
            val models = newTopRated.map { it.toTrendingEntity() }
            clearType(models.first().type)
            upsert(models)
        }
    }

    suspend fun updateTrending(newTrending: List<TrendingItem>) {
        trendingDao.apply {
            val models = newTrending.map { it.toTrendingEntity() }
            clearType(models.first().type)
            upsert(models)
        }
    }

    private fun TrendingEntity.toTrendingItem(): TrendingItem {
        return TrendingItem(
            id = id,
            type = Type.fromString(type),
            synopsis = synopsis,
            canonicalTitle = canonicalTitle,
            subtype = subtype,
            posterImage = posterImage,
            averageRating = averageRating,
            ratingRank = ratingRank,
            popularityRank = popularityRank
        )
    }

    private fun TrendingItem.toTrendingEntity(): TrendingEntity {
        return TrendingEntity(
            id = id,
            type = type.name,
            synopsis = synopsis,
            canonicalTitle = canonicalTitle,
            subtype = subtype,
            posterImage = posterImage,
            averageRating = averageRating,
            ratingRank = ratingRank,
            popularityRank = popularityRank
        )
    }
}
