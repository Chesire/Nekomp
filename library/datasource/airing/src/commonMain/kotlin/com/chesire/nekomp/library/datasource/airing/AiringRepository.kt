package com.chesire.nekomp.library.datasource.airing

import co.touchlab.kermit.Logger
import com.chesire.nekomp.core.model.Titles
import com.chesire.nekomp.core.network.NetworkError
import com.chesire.nekomp.library.datasource.airing.local.AiringStorage
import com.chesire.nekomp.library.datasource.airing.remote.AiringApi
import com.chesire.nekomp.library.datasource.airing.remote.model.SeasonResponseDto
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock

class AiringRepository(
    private val airingStorage: AiringStorage,
    private val airingApi: AiringApi
) {

    val currentAiring: Flow<List<AiringAnime>> = airingStorage.airingEntries

    // TODO: Make worker to call this on app start
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
                    val apiError = it as? NetworkError.Api
                    if (apiError?.code == HttpStatusCode.TooManyRequests.value) {
                        Logger.d("AiringRepository") { "Got rate limited, waiting then trying again" }
                        retries++
                        if (retries == 5) {
                            Logger.d("AiringRepository") { "Retries too high, exiting" }
                            // We tried too many times, something is wrong
                            return Err(Unit)
                        } else {
                            // Rate limited, delay briefly then try again
                            delay(500)
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
            if (diff.inWholeMilliseconds < 600) {
                // Genius
                val waitTime = 600 - diff.inWholeMilliseconds
                Logger.v("AiringRepository") { "Need to wait as too fast, so waiting for $waitTime millis" }
                delay(waitTime)
            }
        } while (hasNextPage)

        Logger.d("AiringRepository") {
            "PopulateCurrentAiring got entries [$isSuccess], total entries ${entries.count()}"
        }
        return if (isSuccess) Ok(entries) else Err(Unit)
    }

    private fun buildAiringEntries(body: SeasonResponseDto): List<AiringAnime> {
        return body.data.map {
            AiringAnime(
                malId = it.malId,
                titles = Titles(
                    canonical = it.title,
                    english = it.titleEnglish ?: "",
                    romaji = "", // No romaji title so just leave blank
                    cjk = it.titleJapanese ?: ""
                )
            )
        }
    }
}
