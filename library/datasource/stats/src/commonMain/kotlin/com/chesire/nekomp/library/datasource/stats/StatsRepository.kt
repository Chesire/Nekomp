package com.chesire.nekomp.library.datasource.stats

import co.touchlab.kermit.Logger
import com.chesire.nekomp.library.datasource.stats.local.StatsStorage
import com.chesire.nekomp.library.datasource.stats.remote.StatsApi
import com.chesire.nekomp.library.datasource.stats.remote.model.StatsKind
import com.chesire.nekomp.library.datasource.user.UserRepository
import kotlinx.coroutines.flow.first

class StatsRepository(
    private val statsApi: StatsApi,
    private val statsStorage: StatsStorage,
    private val userRepository: UserRepository // TODO: Inject method to get the user id?
) {

    suspend fun test() {
        val d = statsApi.retrieveStats(
            userRepository.user.first().id,
            StatsKind.AnimeAmountConsumed.apiString
        )
        Logger.d { "$d" }
    }
}
