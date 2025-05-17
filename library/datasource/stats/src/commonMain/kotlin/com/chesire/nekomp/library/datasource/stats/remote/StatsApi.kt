package com.chesire.nekomp.library.datasource.stats.remote

import com.chesire.nekomp.core.network.NetworkError
import com.github.michaelbull.result.Result
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers

interface StatsApi {

    @Headers(
        "Accept: application/vnd.api+json",
        "Content-Type: application/vnd.api+json"
    )
    @GET(
        "api/edge/favorites" //TODO
    )
    suspend fun retrieveFavoriteCharacters(): Result<Unit, NetworkError>
}
