package com.chesire.nekomp.library.datasource.trending.local

import com.chesire.nekomp.core.database.dao.TrendingDao
import com.chesire.nekomp.core.database.entity.TrendingEntity
import com.chesire.nekomp.core.model.Type
import com.chesire.nekomp.library.datasource.trending.TrendingItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrendingStorage(private val trendingDao: TrendingDao) {

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

    suspend fun updateTrending(newTrending: List<TrendingItem>) {
        trendingDao.apply {
            val models = newTrending.map { it.toTrendingEntity() }
            // clearType(models.first().type) - this needs some more thought...
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
