package com.chesire.nekomp.library.datasource.favorite.remote

import com.chesire.nekomp.core.network.NetworkError
import com.github.michaelbull.result.Result
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.Query

interface FavoriteApi {

    @Headers(
        "Accept: application/vnd.api+json",
        "Content-Type: application/vnd.api+json"
    )
    @GET(
        "api/edge/favorites" +
            "?include=item" +
            "&filter[itemType]=Character" +
            "&fields[item]=canonicalName,image"
    )
    suspend fun retrieveFavoriteCharacters(
        @Query("filter[userId]") userId: Int
    ): Result<String, NetworkError>
}
