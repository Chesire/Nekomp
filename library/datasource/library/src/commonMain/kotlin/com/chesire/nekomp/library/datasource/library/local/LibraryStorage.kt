package com.chesire.nekomp.library.datasource.library.local

import com.chesire.nekomp.core.database.dao.LibraryEntryDao
import com.chesire.nekomp.core.database.entity.LibraryEntryEntity
import com.chesire.nekomp.library.datasource.library.LibraryEntry
import kotlinx.coroutines.flow.map

class LibraryStorage(private val libraryEntryDao: LibraryEntryDao) {

    val libraryEntries = libraryEntryDao
        .series()
        .map { entries ->
            entries.map { entry ->
                LibraryEntry(
                    id = entry.id,
                    userId = entry.userId,
                    type = entry.type,
                    subtype = entry.subtype,
                    slug = entry.slug,
                    title = entry.title,
                    seriesStatus = entry.seriesStatus,
                    userSeriesStatus = entry.userSeriesStatus,
                    progress = entry.progress,
                    totalLength = entry.totalLength,
                    rating = entry.rating,
                    posterImage = entry.posterImage,
                    startDate = entry.startDate,
                    endDate = entry.endDate
                )
            }
        }

    suspend fun updateEntries(entries: List<LibraryEntry>) {
        val newEntries = entries.map { entry ->
            LibraryEntryEntity(
                id = entry.id,
                userId = entry.userId,
                type = entry.type,
                subtype = entry.subtype,
                slug = entry.slug,
                title = entry.title,
                seriesStatus = entry.seriesStatus,
                userSeriesStatus = entry.userSeriesStatus,
                progress = entry.progress,
                totalLength = entry.totalLength,
                rating = entry.rating,
                posterImage = entry.posterImage,
                startDate = entry.startDate,
                endDate = entry.endDate
            )
        }
        libraryEntryDao.upsert(newEntries)
    }
}
