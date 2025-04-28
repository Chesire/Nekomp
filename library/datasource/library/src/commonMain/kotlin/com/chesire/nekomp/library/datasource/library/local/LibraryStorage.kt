package com.chesire.nekomp.library.datasource.library.local

import com.chesire.nekomp.core.database.dao.LibraryEntryDao
import com.chesire.nekomp.core.database.entity.LibraryEntryEntity
import com.chesire.nekomp.core.model.EntryStatus
import com.chesire.nekomp.core.model.Image
import com.chesire.nekomp.core.model.Titles
import com.chesire.nekomp.core.model.Type
import com.chesire.nekomp.library.datasource.library.LibraryEntry
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant

class LibraryStorage(private val libraryEntryDao: LibraryEntryDao) {

    val libraryEntries = libraryEntryDao
        .entries()
        .map { entries ->
            entries.map { entry ->
                LibraryEntry(
                    id = entry.id,
                    entryId = entry.entryId,
                    type = Type.fromString(entry.type),
                    updatedAt = Instant.parse(entry.updatedAt),
                    primaryType = entry.primaryType,
                    subtype = entry.subtype,
                    slug = entry.slug,
                    titles = Titles(
                        canonical = entry.canonicalTitle,
                        english = entry.englishTitle,
                        romaji = entry.romajiTitle,
                        cjk = entry.cjkTitle
                    ),
                    seriesStatus = entry.seriesStatus,
                    entryStatus = EntryStatus.fromString(entry.entryStatus),
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
                    coverImage = Image(
                        tiny = entry.coverImageTiny,
                        small = entry.coverImageSmall,
                        medium = entry.coverImageMedium,
                        large = entry.coverImageLarge,
                        original = entry.coverImageOriginal,
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
                entryId = entry.entryId,
                type = entry.type.name,
                updatedAt = entry.updatedAt.toString(),
                primaryType = entry.primaryType,
                subtype = entry.subtype,
                slug = entry.slug,
                canonicalTitle = entry.titles.canonical,
                englishTitle = entry.titles.english,
                romajiTitle = entry.titles.romaji,
                cjkTitle = entry.titles.cjk,
                seriesStatus = entry.seriesStatus,
                entryStatus = entry.entryStatus.name,
                progress = entry.progress,
                totalLength = entry.totalLength,
                rating = entry.rating,
                posterImageTiny = entry.posterImage.tiny,
                posterImageSmall = entry.posterImage.small,
                posterImageMedium = entry.posterImage.medium,
                posterImageLarge = entry.posterImage.large,
                posterImageOriginal = entry.posterImage.original,
                coverImageTiny = entry.coverImage.tiny,
                coverImageSmall = entry.coverImage.small,
                coverImageMedium = entry.coverImage.medium,
                coverImageLarge = entry.coverImage.large,
                coverImageOriginal = entry.coverImage.original,
                startDate = entry.startDate,
                endDate = entry.endDate
            )
        }
        libraryEntryDao.upsert(newEntries)
    }

    suspend fun updateEntry(entry: LibraryEntry) {
        val newEntry = LibraryEntryEntity(
            id = entry.id,
            entryId = entry.entryId,
            type = entry.type.name,
            updatedAt = entry.updatedAt.toString(),
            primaryType = entry.primaryType,
            subtype = entry.subtype,
            slug = entry.slug,
            canonicalTitle = entry.titles.canonical,
            englishTitle = entry.titles.english,
            romajiTitle = entry.titles.romaji,
            cjkTitle = entry.titles.cjk,
            seriesStatus = entry.seriesStatus,
            entryStatus = entry.entryStatus.name,
            progress = entry.progress,
            totalLength = entry.totalLength,
            rating = entry.rating,
            posterImageTiny = entry.posterImage.tiny,
            posterImageSmall = entry.posterImage.small,
            posterImageMedium = entry.posterImage.medium,
            posterImageLarge = entry.posterImage.large,
            posterImageOriginal = entry.posterImage.original,
            coverImageTiny = entry.coverImage.tiny,
            coverImageSmall = entry.coverImage.small,
            coverImageMedium = entry.coverImage.medium,
            coverImageLarge = entry.coverImage.large,
            coverImageOriginal = entry.coverImage.original,
            startDate = entry.startDate,
            endDate = entry.endDate
        )
        libraryEntryDao.upsert(newEntry)
    }

    suspend fun deleteEntry(entryId: Int) {
        libraryEntryDao.delete(entryId = entryId)
    }
}
