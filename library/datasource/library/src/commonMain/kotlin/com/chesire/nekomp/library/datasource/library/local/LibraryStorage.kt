package com.chesire.nekomp.library.datasource.library.local

import com.chesire.nekomp.core.database.dao.LibraryEntryDao
import com.chesire.nekomp.core.database.entity.LibraryEntryEntity
import com.chesire.nekomp.core.model.Image
import com.chesire.nekomp.core.model.Title
import com.chesire.nekomp.core.model.Type
import com.chesire.nekomp.library.datasource.library.LibraryEntry
import kotlinx.coroutines.flow.map

class LibraryStorage(private val libraryEntryDao: LibraryEntryDao) {

    val libraryEntries = libraryEntryDao
        .entries()
        .map { entries ->
            entries.map { entry ->
                LibraryEntry(
                    id = entry.id,
                    userId = entry.userId,
                    type = Type.fromString(entry.type),
                    primaryType = entry.primaryType,
                    subtype = entry.subtype,
                    slug = entry.slug,
                    title = entry.title,
                    titles = Title(
                        english = entry.englishTitle,
                        romaji = entry.romajiTitle,
                        japanese = entry.japaneseTitle
                    ),
                    seriesStatus = entry.seriesStatus,
                    userSeriesStatus = entry.userSeriesStatus,
                    progress = entry.progress,
                    totalLength = entry.totalLength,
                    rating = entry.rating,
                    posterImage = Image(
                        tiny = entry.posterImageTiny,
                        small = entry.posterImageSmall,
                        medium = entry.posterImageMedium,
                        large = entry.posterImageLarge,
                        original = entry.posterImageOriginal,
                    ),
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
                type = entry.type.name,
                primaryType = entry.primaryType,
                subtype = entry.subtype,
                slug = entry.slug,
                title = entry.title,
                englishTitle = entry.titles.english,
                romajiTitle = entry.titles.romaji,
                japaneseTitle = entry.titles.japanese,
                seriesStatus = entry.seriesStatus,
                userSeriesStatus = entry.userSeriesStatus,
                progress = entry.progress,
                totalLength = entry.totalLength,
                rating = entry.rating,
                posterImageTiny = entry.posterImage.tiny,
                posterImageSmall = entry.posterImage.small,
                posterImageMedium = entry.posterImage.medium,
                posterImageLarge = entry.posterImage.large,
                posterImageOriginal = entry.posterImage.original,
                startDate = entry.startDate,
                endDate = entry.endDate
            )
        }
        libraryEntryDao.upsert(newEntries)
    }

    suspend fun updateEntry(entry: LibraryEntry) {
        val newEntry = LibraryEntryEntity(
            id = entry.id,
            userId = entry.userId,
            type = entry.type.name,
            primaryType = entry.primaryType,
            subtype = entry.subtype,
            slug = entry.slug,
            title = entry.title,
            englishTitle = entry.titles.english,
            romajiTitle = entry.titles.romaji,
            japaneseTitle = entry.titles.japanese,
            seriesStatus = entry.seriesStatus,
            userSeriesStatus = entry.userSeriesStatus,
            progress = entry.progress,
            totalLength = entry.totalLength,
            rating = entry.rating,
            posterImageTiny = entry.posterImage.tiny,
            posterImageSmall = entry.posterImage.small,
            posterImageMedium = entry.posterImage.medium,
            posterImageLarge = entry.posterImage.large,
            posterImageOriginal = entry.posterImage.original,
            startDate = entry.startDate,
            endDate = entry.endDate
        )
        libraryEntryDao.upsert(newEntry)
    }
}
