package com.chesire.nekomp.library.datasource.library.remote

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.Path

interface LibraryApi {

    @Headers(
        "Accept: application/vnd.api+json",
        "Content-Type: application/vnd.api+json"
    )
    @GET("api/edge/users/{userId}/library-entries?filter[kind]=anime")
    suspend fun retrieve(@Path("userId") userId: Int): Result<String>
    // filter[user_id]=userId maybe?
}
