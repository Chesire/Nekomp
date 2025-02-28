package com.chesire.nekomp.library.datasource.trending.remote

import com.chesire.nekomp.library.datasource.trending.remote.model.TrendingResponseDto
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers

private const val FIELDS =
    "synopsis,titles,canonicalTitle,subtype,posterImage,coverImage,averageRating,ratingRank,popularityRank"

interface TrendingApi {

    @Headers(
        "Accept: application/vnd.api+json",
        "Content-Type: application/vnd.api+json"
    )
    @GET("api/edge/trending/anime?fields[anime]=$FIELDS&limit=20")
    suspend fun trendingAnime(): Result<TrendingResponseDto>

    @Headers(
        "Accept: application/vnd.api+json",
        "Content-Type: application/vnd.api+json"
    )
    @GET("api/edge/trending/manga?fields[manga]=$FIELDS&limit=20")
    suspend fun trendingManga(): Result<TrendingResponseDto>

    @Headers(
        "Accept: application/vnd.api+json",
        "Content-Type: application/vnd.api+json"
    )
    @GET("api/edge/anime?fields[anime]=$FIELDS&sort=ratingRank&limit=20")
    suspend fun topRatedAnime(): Result<TrendingResponseDto>

    @Headers(
        "Accept: application/vnd.api+json",
        "Content-Type: application/vnd.api+json"
    )
    @GET("api/edge/manga?fields[manga]=$FIELDS&sort=ratingRank&limit=20")
    suspend fun topRatedManga(): Result<TrendingResponseDto>

    @Headers(
        "Accept: application/vnd.api+json",
        "Content-Type: application/vnd.api+json"
    )
    @GET("api/edge/anime?fields[anime]=$FIELDS&sort=popularityRank&limit=20")
    suspend fun mostPopularAnime(): Result<TrendingResponseDto>

    @Headers(
        "Accept: application/vnd.api+json",
        "Content-Type: application/vnd.api+json"
    )
    @GET("api/edge/manga?fields[manga]=$FIELDS&sort=popularityRank&limit=20")
    suspend fun mostPopularManga(): Result<TrendingResponseDto>
}
