package com.chesire.nekomp.library.datasource.airing.remote

import com.chesire.nekomp.library.datasource.airing.remote.model.SeasonResponseDto
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.Query

interface AiringApi {

    @Headers(
        "Accept: application/vnd.api+json",
        "Content-Type: application/vnd.api+json"
    )
    @GET("v4/seasons/now?filter=tv&continuing")
    suspend fun retrieveSeasonNow(@Query("page") page: Int): Result<SeasonResponseDto>
}
