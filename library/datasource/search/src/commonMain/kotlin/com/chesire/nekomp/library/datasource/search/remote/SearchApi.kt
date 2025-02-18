package com.chesire.nekomp.library.datasource.search.remote

import com.chesire.nekomp.library.datasource.search.remote.model.SearchResponseDto
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query

private const val FIELDS = "synopsis,titles,canonicalTitle,subtype,posterImage"

// TODO: These calls should support pagination, might be good to add at some point
interface SearchApi {

    @GET("api/edge/anime?fields[anime]=$FIELDS")
    suspend fun searchForAnime(
        @Query("filter[text]") title: String
    ): Result<SearchResponseDto>

    @GET("api/edge/manga?fields[manga]=$FIELDS")
    suspend fun searchForManga(
        @Query("filter[text]") title: String
    ): Result<SearchResponseDto>
}
