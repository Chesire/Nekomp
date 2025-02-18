package com.chesire.nekomp.library.datasource.library.remote

import com.chesire.nekomp.library.datasource.library.remote.model.AddEntryResponseDto
import com.chesire.nekomp.library.datasource.library.remote.model.RetrieveResponseDto
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

private const val FIELDS =
    "slug,titles,canonicalTitle,startDate,endDate,subtype,status,posterImage"

interface LibraryApi {

    @Headers(
        "Accept: application/vnd.api+json",
        "Content-Type: application/vnd.api+json"
    )
    @GET(
        "api/edge/users/{userId}/library-entries" +
            "?include=anime" +
            "&fields[libraryEntries]=status,progress,anime,startedAt,finishedAt,ratingTwenty" +
            "&fields[anime]=$FIELDS,episodeCount" +
            "&filter[kind]=anime" +
            "&sort=anime.titles.canonical"
    )
    suspend fun retrieveAnime(
        @Path("userId") userId: Int,
        @Query("page[offset]") offset: Int,
        @Query("page[limit]") limit: Int
    ): Result<RetrieveResponseDto>

    @Headers(
        "Accept: application/vnd.api+json",
        "Content-Type: application/vnd.api+json"
    )
    @GET(
        "api/edge/users/{userId}/library-entries" +
            "?include=manga" +
            "&fields[libraryEntries]=status,progress,manga,startedAt,finishedAt,ratingTwenty" +
            "&fields[manga]=$FIELDS,chapterCount" +
            "&filter[kind]=manga" +
            "&sort=manga.titles.canonical"
    )
    suspend fun retrieveManga(
        @Path("userId") userId: Int,
        @Query("page[offset]") offset: Int,
        @Query("page[limit]") limit: Int
    ): Result<RetrieveResponseDto>

    @Headers(
        "Accept: application/vnd.api+json",
        "Content-Type: application/vnd.api+json"
    )
    @POST(
        "api/edge/library-entries" +
            "?include=anime" +
            "&fields[anime]=$FIELDS,episodeCount"
    )
    suspend fun addAnime(@Body data: String): Result<AddEntryResponseDto>

    @Headers(
        "Accept: application/vnd.api+json",
        "Content-Type: application/vnd.api+json"
    )
    @POST(
        "api/edge/library-entries" +
            "?include=manga" +
            "&fields[manga]=$FIELDS,chapterCount"
    )
    suspend fun addManga(@Body data: String): Result<AddEntryResponseDto>
}
