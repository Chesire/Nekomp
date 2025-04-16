package com.chesire.nekomp.library.datasource.airing.local

import co.touchlab.kermit.Logger
import com.chesire.nekomp.core.database.dao.AiringDao
import com.chesire.nekomp.core.database.entity.AiringEntity
import com.chesire.nekomp.core.model.AiringTime
import com.chesire.nekomp.core.model.Image
import com.chesire.nekomp.core.model.Season
import com.chesire.nekomp.core.model.Titles
import com.chesire.nekomp.library.datasource.airing.AiringAnime
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.isoDayNumber

class AiringStorage(private val airingDao: AiringDao) {

    // TODO: Need to clear out any entries which have airing == false every now and then

    val airingEntries = airingDao
        .entries()
        .map { entries ->
            entries.map { entry ->
                AiringAnime(
                    malId = entry.malId,
                    kitsuId = entry.kitsuId,
                    titles = Titles(
                        canonical = entry.canonicalTitle,
                        english = entry.englishTitle,
                        romaji = entry.romajiTitle,
                        cjk = entry.cjkTitle
                    ),
                    posterImage = Image(
                        tiny = entry.posterImageTiny,
                        small = entry.posterImageSmall,
                        medium = entry.posterImageMedium,
                        large = entry.posterImageLarge,
                        original = entry.posterImageOriginal,
                    ),
                    airing = entry.airing,
                    season = Season.fromString(entry.season),
                    year = entry.year,
                    airingTime = if (entry.airingHour == -1) {
                        null
                    } else {
                        AiringTime(
                            dayOfWeek = DayOfWeek(entry.airingDayOfWeek),
                            hour = entry.airingHour,
                            minute = entry.airingMinute,
                            timeZone = entry.airingTimeZone
                        )
                    }
                )
            }
        }

    suspend fun updateEntries(entries: List<AiringAnime>) {
        val newEntries = entries.map { entry ->
            AiringEntity(
                malId = entry.malId,
                kitsuId = entry.kitsuId,
                canonicalTitle = entry.titles.canonical,
                englishTitle = entry.titles.english,
                romajiTitle = entry.titles.romaji,
                cjkTitle = entry.titles.cjk,
                posterImageTiny = entry.posterImage.tiny,
                posterImageSmall = entry.posterImage.small,
                posterImageMedium = entry.posterImage.medium,
                posterImageLarge = entry.posterImage.large,
                posterImageOriginal = entry.posterImage.original,
                airing = entry.airing,
                season = entry.season.name,
                year = entry.year,
                airingDayOfWeek = entry.airingTime?.dayOfWeek?.isoDayNumber ?: 1,
                airingHour = entry.airingTime?.hour ?: -1,
                airingMinute = entry.airingTime?.minute ?: -1,
                airingTimeZone = entry.airingTime?.timeZone ?: "",
            )
        }

        airingDao.upsert(newEntries)
    }

    suspend fun clearOld(newEntries: List<AiringAnime>) {
        val currentAiring = airingDao.entriesSync().map { it.malId }
        val newAiring = newEntries.map { it.malId }
        val deleteIds = currentAiring.subtract(newAiring).toList()
        Logger.d("AiringStorage") {
            "Checking to delete old entries::\n" +
                "Current series - [${currentAiring.joinToString()}]\n" +
                "New series     - [${newAiring.joinToString()}]\n" +
                "Removing ids   - [${deleteIds.joinToString()}]"
        }
        val deleted = airingDao.delete(deleteIds)
        Logger.d("AiringStorage") { "Deleted $deleted old entries" }
    }
}
