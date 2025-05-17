package com.chesire.nekomp.library.datasource.stats

import com.chesire.nekomp.library.datasource.stats.local.StatsStorage
import com.chesire.nekomp.library.datasource.stats.remote.StatsApi
import com.chesire.nekomp.library.datasource.user.UserRepository

class StatsRepository(
    private val statsApi: StatsApi,
    private val statsStorage: StatsStorage,
    private val userRepository: UserRepository // TODO: Inject method to get the user id?
) {
}
