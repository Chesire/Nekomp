package com.chesire.nekomp.library.datasource.trending.local

import co.touchlab.kermit.Logger
import com.chesire.nekomp.core.database.dao.TrendingDao
import com.chesire.nekomp.core.database.entity.TrendingEntity
import com.chesire.nekomp.core.model.Image
import com.chesire.nekomp.core.model.Titles
import com.chesire.nekomp.core.model.Type
import com.chesire.nekomp.library.datasource.trending.TrendingItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrendingStorage(private val trendingDao: TrendingDao) {

    val trendingAnime: Flow<List<TrendingItem>> = trendingDao
        .trending(Type.Anime.name)
        .map { items ->
            items.map { it.toTrendingItem() }
        }

    val trendingManga: Flow<List<TrendingItem>> = trendingDao
        .trending(Type.Manga.name)
        .map { items ->
            items.map { it.toTrendingItem() }
        }

    suspend fun updateTrending(newTrending: List<TrendingItem>) {
        Logger.d("TrendingStorage") { "Storing ${newTrending.count()} trending items" }
        val models = newTrending.map { it.toTrendingEntity() }
        trendingDao.upsert(models)
    }

    suspend fun clearLegacyData() {
        Logger.d("TrendingStorage") { "Executing call to clear trending dao" }
        val clearAmount = trendingDao.delete()
        Logger.d("TrendingStorage") { "Finished delete call, cleared $clearAmount entries" }
    }

    private fun TrendingEntity.toTrendingItem(): TrendingItem {
        return TrendingItem(
            id = id,
            type = Type.fromString(type),
            synopsis = synopsis,
            titles = Titles(
                canonical = canonicalTitle,
                english = englishTitle,
                romaji = romajiTitle,
                cjk = cjkTitle
            ),
            subtype = subtype,
            status = status,
            posterImage = Image(
                tiny = posterImageTiny,
                small = posterImageSmall,
                medium = posterImageMedium,
                large = posterImageLarge,
                original = posterImageOriginal
            ),
            coverImage = Image(
                tiny = coverImageTiny,
                small = coverImageSmall,
                medium = coverImageMedium,
                large = coverImageLarge,
                original = coverImageOriginal
            ),
            totalLength = totalLength,
            averageRating = averageRating,
            ratingRank = ratingRank,
            popularityRank = popularityRank,
            trendingRank = trendingRank ?: -1
        )
    }

    private fun TrendingItem.toTrendingEntity(): TrendingEntity {
        return TrendingEntity(
            id = id,
            type = type.name,
            synopsis = synopsis,
            canonicalTitle = titles.canonical,
            englishTitle = titles.english,
            romajiTitle = titles.romaji,
            cjkTitle = titles.cjk,
            subtype = subtype,
            status = status,
            posterImageTiny = posterImage.tiny,
            posterImageSmall = posterImage.small,
            posterImageMedium = posterImage.medium,
            posterImageLarge = posterImage.large,
            posterImageOriginal = posterImage.original,
            coverImageTiny = coverImage.tiny,
            coverImageSmall = coverImage.small,
            coverImageMedium = coverImage.medium,
            coverImageLarge = coverImage.large,
            coverImageOriginal = coverImage.original,
            totalLength = totalLength,
            averageRating = averageRating,
            ratingRank = ratingRank,
            popularityRank = popularityRank,
            trendingRank = if (trendingRank == -1) null else trendingRank
        )
    }
}
