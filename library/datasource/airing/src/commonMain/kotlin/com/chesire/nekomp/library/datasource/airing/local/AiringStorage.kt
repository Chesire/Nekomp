package com.chesire.nekomp.library.datasource.airing.local

import com.chesire.nekomp.core.database.dao.AiringDao
import com.chesire.nekomp.core.database.entity.AiringEntity
import com.chesire.nekomp.core.model.Titles
import com.chesire.nekomp.library.datasource.airing.AiringAnime
import kotlinx.coroutines.flow.map

class AiringStorage(private val airingDao: AiringDao) {

    // TODO: Need to clear this out every now and then

    val airingEntries = airingDao
        .entries()
        .map { entries ->
            entries.map { entry ->
                AiringAnime(
                    malId = entry.malId,
                    titles = Titles(
                        canonical = entry.canonicalTitle,
                        english = entry.englishTitle,
                        romaji = entry.romajiTitle,
                        cjk = entry.cjkTitle
                    )
                )
            }
        }

    suspend fun updateEntries(entries: List<AiringAnime>) {
        val newEntries = entries.map { entry ->
            AiringEntity(
                malId = entry.malId,
                canonicalTitle = entry.titles.canonical,
                englishTitle = entry.titles.english,
                romajiTitle = entry.titles.romaji,
                cjkTitle = entry.titles.cjk,
            )
        }
        airingDao.upsert(newEntries)
    }
}
