package com.chesire.nekomp.library.datasource.stats.local

import co.touchlab.kermit.Logger
import com.chesire.nekomp.core.database.dao.ConsumedDao
import com.chesire.nekomp.core.database.entity.ConsumedDataEntity
import com.chesire.nekomp.library.datasource.stats.ConsumedStats
import com.chesire.nekomp.library.datasource.stats.ConsumedStatsType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StatsStorage(private val consumedDao: ConsumedDao) {

    val animeConsumedStats: Flow<ConsumedStats> = consumedDao
        .entries(ConsumedStatsType.Anime.name.lowercase())
        .map { entry -> entry.toConsumedStats() }

    val mangaConsumedStats: Flow<ConsumedStats> = consumedDao
        .entries(ConsumedStatsType.Manga.name.lowercase())
        .map { entry -> entry.toConsumedStats() }

    suspend fun updateConsumed(newConsumedStats: ConsumedStats) {
        Logger.d("StatsStorage") { "Inserting $newConsumedStats into the dao" }
        consumedDao.upsert(newConsumedStats.toConsumedDataEntity())
    }

    private fun ConsumedDataEntity.toConsumedStats(): ConsumedStats {
        return ConsumedStats(
            type = ConsumedStatsType.fromString(type),
            time = time,
            media = media,
            units = units,
            completed = completed
        )
    }

    private fun ConsumedStats.toConsumedDataEntity(): ConsumedDataEntity {
        return ConsumedDataEntity(
            type = type.name,
            time = time,
            media = media,
            units = units,
            completed = completed
        )
    }
}
