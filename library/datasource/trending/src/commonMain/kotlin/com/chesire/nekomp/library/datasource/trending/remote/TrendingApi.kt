package com.chesire.nekomp.library.datasource.trending.remote

import com.chesire.nekomp.library.datasource.trending.remote.model.TrendingResponseDto
import de.jensklingenberg.ktorfit.http.GET

private const val FIELDS = "synopsis,titles,canonicalTitle,subtype,posterImage"

interface TrendingApi {

    @GET("api/edge/trending/anime?fields[anime]=$FIELDS")
    suspend fun trendingAnime(): Result<TrendingResponseDto>

    @GET("api/edge/trending/manga?fields[manga]=$FIELDS")
    suspend fun trendingManga(): Result<TrendingResponseDto>
}
