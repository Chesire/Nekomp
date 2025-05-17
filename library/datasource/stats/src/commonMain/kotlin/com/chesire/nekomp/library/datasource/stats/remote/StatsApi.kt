package com.chesire.nekomp.library.datasource.stats.remote

import com.chesire.nekomp.core.network.NetworkError
import com.chesire.nekomp.library.datasource.stats.remote.model.RetrieveStatsResponseDto
import com.github.michaelbull.result.Result
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.Query

interface StatsApi {

    @Headers(
        "Accept: application/vnd.api+json",
        "Content-Type: application/vnd.api+json"
    )
    @GET("api/edge/stats")
    suspend fun retrieveStats(
        @Query("filter[userId]") userId: Int,
        @Query("filter[kind]") kind: String // See StatsKind
    ): Result<RetrieveStatsResponseDto, NetworkError>
}
