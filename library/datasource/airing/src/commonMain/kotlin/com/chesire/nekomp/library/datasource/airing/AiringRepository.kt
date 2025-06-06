package com.chesire.nekomp.library.datasource.airing

import co.touchlab.kermit.Logger
import com.chesire.nekomp.core.database.dao.MappingDao
import com.chesire.nekomp.core.model.AiringTime
import com.chesire.nekomp.core.model.Image
import com.chesire.nekomp.core.model.Season
import com.chesire.nekomp.core.model.Titles
import com.chesire.nekomp.core.network.NetworkError
import com.chesire.nekomp.library.datasource.airing.local.AiringStorage
import com.chesire.nekomp.library.datasource.airing.remote.AiringApi
import com.chesire.nekomp.library.datasource.airing.remote.model.SeasonResponseDto
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.map
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.char

private const val MAX_RETRIES = 5
private const val REQUEST_FAILED_DELAY_MILLIS = 500L
private const val REQUEST_TIME_MILLIS = 600L

class AiringRepository(
    private val airingStorage: AiringStorage,
    private val airingApi: AiringApi,
    private val mappingDao: MappingDao
) {

    val currentAiring: Flow<List<AiringAnime>> = airingStorage.airingEntries

    @Suppress("ReturnCount")
    suspend fun syncCurrentAiring(): Result<List<AiringAnime>, Unit> {
        Logger.d("AiringRepository") { "Populating the airing data" }
        var page = 1
        var hasNextPage = true
        var isSuccess = false
        var retries = 0
        val entries = mutableListOf<AiringAnime>()
        do {
            val start = Clock.System.now()
            airingApi.retrieveSeasonNow(page)
                .map { dto ->
                    hasNextPage = dto.pagination.hasNextPage
                    page = dto.pagination.currentPage + 1
                    if (dto.data.isEmpty()) {
                        emptyList()
                    } else {
                        buildAiringEntries(dto)
                    }
                }
                .onSuccess {
                    isSuccess = true
                    retries = 0
                    entries.addAll(it)
                    airingStorage.updateEntries(it)
                }
                .onFailure {
                    val apiError = it as? NetworkError.ApiError
                    if (apiError?.code == HttpStatusCode.TooManyRequests.value) {
                        Logger.d("AiringRepository") { "Got rate limited, waiting then trying again" }
                        retries++
                        if (retries == MAX_RETRIES) {
                            Logger.d("AiringRepository") { "Retries too high, exiting" }
                            // We tried too many times, something is wrong
                            return Err(Unit)
                        } else {
                            // Rate limited, delay briefly then try again
                            delay(REQUEST_FAILED_DELAY_MILLIS)
                        }
                    } else {
                        // Unexpected error case, exit out
                        return Err(Unit)
                    }
                }

            val end = Clock.System.now()
            val diff = end - start
            Logger.v("AiringRepository") { "Request completed in ${diff.inWholeMilliseconds} milliseconds" }
            // Rate limited to 3 requests per second, so need to make sure we delay between calls
            if (diff.inWholeMilliseconds < REQUEST_TIME_MILLIS) {
                // Genius
                val waitTime = REQUEST_TIME_MILLIS - diff.inWholeMilliseconds
                Logger.v("AiringRepository") { "Need to wait as too fast, so waiting for $waitTime millis" }
                delay(waitTime)
            }
        } while (hasNextPage)

        Logger.d("AiringRepository") {
            "PopulateCurrentAiring got entries [$isSuccess], total entries ${entries.count()}"
        }
        return if (isSuccess) {
            airingStorage.clearOld(entries)
            Ok(entries)
        } else {
            Err(Unit)
        }
    }

    @Suppress("SwallowedException")
    private suspend fun buildAiringEntries(body: SeasonResponseDto): List<AiringAnime> {
        val timeFormat = DateTimeComponents.Format {
            dayOfWeek(
                DayOfWeekNames(
                    monday = "Mondays",
                    tuesday = "Tuesdays",
                    wednesday = "Wednesdays",
                    thursday = "Thursdays",
                    friday = "Fridays",
                    saturday = "Saturdays",
                    sunday = "Sundays"
                )
            )
            char(' ')
            hour()
            char(':')
            minute()
            char(' ')
            timeZoneId()
        }

        return body
            .data
            .filterNot { it.broadcast?.day == null }
            .map {
                AiringAnime(
                    malId = it.malId,
                    kitsuId = mappingDao.entityFromMalId(it.malId)?.kitsuId,
                    titles = Titles(
                        canonical = it.title,
                        english = it.titleEnglish ?: "",
                        romaji = "", // No romaji title so just leave blank
                        cjk = it.titleJapanese ?: ""
                    ),
                    posterImage = Image(
                        tiny = "",
                        small = "",
                        medium = it.images?.webp?.defaultImage ?: it.images?.jpg?.defaultImage
                        ?: "",
                        large = it.images?.webp?.largeImage ?: it.images?.jpg?.largeImage ?: "",
                        original = ""
                    ),
                    airing = it.airing,
                    season = Season.fromString(it.season),
                    year = it.year ?: -1,
                    airingTime = it.broadcast?.let { broadcast ->
                        val input = "${broadcast.day} ${broadcast.time} ${broadcast.timezone}"

                        try {
                            val parsed = timeFormat.parse(input)
                            val dayOfWeek = requireNotNull(parsed.dayOfWeek)
                            val hour = requireNotNull(parsed.hour)
                            val minute = requireNotNull(parsed.minute)
                            val timeZone = requireNotNull(parsed.timeZoneId)
                            AiringTime(
                                dayOfWeek = dayOfWeek,
                                hour = hour,
                                minute = minute,
                                timeZone = timeZone
                            )
                        } catch (_: IllegalArgumentException) {
                            Logger.e("AiringRepository") { "Failed to parse the time - $input" }
                            null
                        }
                    }
                )
            }
    }
}
