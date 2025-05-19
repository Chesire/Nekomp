package com.chesire.nekomp.library.datasource.stats

import co.touchlab.kermit.Logger
import com.chesire.nekomp.library.datasource.stats.local.StatsStorage
import com.chesire.nekomp.library.datasource.stats.remote.StatsApi
import com.chesire.nekomp.library.datasource.stats.remote.model.RetrieveStatsResponseDto
import com.chesire.nekomp.library.datasource.stats.remote.model.StatsKind
import com.chesire.nekomp.library.datasource.user.UserRepository
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapEither
import com.github.michaelbull.result.onSuccess
import kotlinx.coroutines.flow.firstOrNull

class StatsRepository(
    private val statsApi: StatsApi,
    private val statsStorage: StatsStorage,
    private val userRepository: UserRepository // TODO: Inject method to get the user id?
) {

    val consumedAnime = statsStorage.animeConsumedStats
    val consumedManga = statsStorage.mangaConsumedStats

    suspend fun retrieveConsumedStats(): Result<Unit, Unit> {
        val user = userRepository.user.firstOrNull()
        if (user?.isAuthenticated != true) {
            Logger.e("StatsRepository") { "No user object, cancelling retrieve" }
            return Err(Unit) // TODO: Add custom error type
        }

        return statsApi.retrieveStats(user.id)
            .onSuccess { dto ->
                dto.toConsumedStats(
                    StatsKind.AnimeAmountConsumed,
                    ConsumedStatsType.Anime
                )?.let {
                    statsStorage.updateConsumed(it)
                }
                dto.toConsumedStats(
                    StatsKind.MangaAmountConsumed,
                    ConsumedStatsType.Manga
                )?.let {
                    statsStorage.updateConsumed(it)
                }
            }
            .mapEither(
                success = { Unit },
                failure = { Unit }
            )
    }

    private fun RetrieveStatsResponseDto.toConsumedStats(
        kind: StatsKind,
        statsType: ConsumedStatsType
    ): ConsumedStats? {
        return data
            .find { it.attributes.kind == kind.apiString }
            ?.let { data ->
                ConsumedStats(
                    type = statsType,
                    time = data.attributes.statsData.time,
                    media = data.attributes.statsData.media,
                    units = data.attributes.statsData.units,
                    completed = data.attributes.statsData.completed
                )
            }
    }
}
