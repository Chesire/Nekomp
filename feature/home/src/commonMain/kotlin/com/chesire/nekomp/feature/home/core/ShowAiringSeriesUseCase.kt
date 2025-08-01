package com.chesire.nekomp.feature.home.core

import co.touchlab.kermit.Logger
import com.chesire.nekomp.core.coroutines.emitLatestPeriodically
import com.chesire.nekomp.core.ext.airingAt
import com.chesire.nekomp.core.ext.toBestImage
import com.chesire.nekomp.core.ext.toChosenLanguage
import com.chesire.nekomp.core.model.AiringTime
import com.chesire.nekomp.core.model.EntryStatus
import com.chesire.nekomp.core.model.Type
import com.chesire.nekomp.core.preferences.ApplicationSettings
import com.chesire.nekomp.feature.home.ui.AiringItem
import com.chesire.nekomp.library.datasource.airing.AiringAnime
import com.chesire.nekomp.library.datasource.airing.AiringRepository
import com.chesire.nekomp.library.datasource.library.LibraryEntry
import com.chesire.nekomp.library.datasource.library.LibraryRepository
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

// This is doing more than it should, but i'm lazy
class ShowAiringSeriesUseCase(
    private val libraryRepository: LibraryRepository,
    private val airingRepository: AiringRepository,
    private val applicationSettings: ApplicationSettings
) {

    operator fun invoke(): Flow<List<AiringItem>> {
        return airingRepository
            .currentAiring
            .emitLatestPeriodically(1.toDuration(DurationUnit.MINUTES)) // For the time
            .combine(libraryRepository.libraryEntries) { airing, libraryEntries ->
                val imageQuality = applicationSettings.imageQuality.first()
                val titleLanguage = applicationSettings.titleLanguage.first()
                Logger.d("ShowAiringSeriesUseCase") { "Got new list of airing series - $airing" }

                airing
                    .filter { it.airingTime != null }
                    .filter { it.isTrackedSeries(libraryEntries) }
                    .map { airingItem ->
                        val entry = libraryEntries
                            .filter { it.type == Type.Anime }
                            .find { it.id == airingItem.kitsuId }!!
                        val timeFrame = airingItem.airingTime!!.timeTillShowing()
                        AiringItem(
                            entryId = entry.entryId,
                            title = entry.titles.toChosenLanguage(titleLanguage),
                            coverImage = entry
                                .coverImage
                                .toBestImage(imageQuality)
                                .ifBlank {
                                    airingItem
                                        .posterImage
                                        .toBestImage(imageQuality)
                                },
                            airingAt = parseForAiringAt(timeFrame),
                            minutesTillAir = timeFrame.inWholeMinutes,
                        )
                    }
                    .sortedBy { it.minutesTillAir }
            }
    }

    private fun AiringAnime.isTrackedSeries(libraryEntries: List<LibraryEntry>): Boolean {
        return libraryEntries
            .filter { it.type == Type.Anime }
            .filter { it.entryStatus == EntryStatus.Current || it.entryStatus == EntryStatus.Planned }
            .any { it.id == this.kitsuId }
    }

    private fun AiringTime.timeTillShowing(): Duration {
        return airingAt()
            .toInstant(TimeZone.currentSystemDefault())
            .minus(Clock.System.now())
    }

    @Suppress("MagicNumber")
    private fun parseForAiringAt(timeTillShowing: Duration): String {
        return when {
            timeTillShowing.inWholeDays > 0 -> {
                "In ${timeTillShowing.inWholeDays} days, ${timeTillShowing.inWholeHours % 24} hours"
            }

            else -> buildString {
                append("In ")
                if (timeTillShowing.inWholeHours > 0) {
                    append("${timeTillShowing.inWholeHours} hours, ")
                }
                append("${timeTillShowing.inWholeMinutes % 60} mins")
            }
        }
    }
}
