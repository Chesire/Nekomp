package com.chesire.nekomp.library.datasource.search.remote

import com.chesire.nekomp.core.network.NetworkError
import com.chesire.nekomp.library.datasource.search.remote.model.SearchResponseDto
import com.github.michaelbull.result.Result
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.Query

private const val FIELDS = "synopsis,titles,canonicalTitle,subtype,posterImage,coverImage"

// TODO: These calls should support pagination, might be good to add at some point
interface SearchApi {

    @Headers(
        "Accept: application/vnd.api+json",
        "Content-Type: application/vnd.api+json"
    )
    @GET("api/edge/anime?fields[anime]=$FIELDS")
    suspend fun searchForAnime(
        @Query("filter[text]") title: String
    ): Result<SearchResponseDto, NetworkError>

    @Headers(
        "Accept: application/vnd.api+json",
        "Content-Type: application/vnd.api+json"
    )
    @GET("api/edge/manga?fields[manga]=$FIELDS")
    suspend fun searchForManga(
        @Query("filter[text]") title: String
    ): Result<SearchResponseDto, NetworkError>
}
