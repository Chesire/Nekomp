package com.chesire.nekomp.library.datasource.user.remote

import com.chesire.nekomp.library.datasource.user.remote.model.UserResponseDto
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers

interface UserApi {

    @Headers(
        "Accept: application/vnd.api+json",
        "Content-Type: application/vnd.api+json"
    )
    @GET("api/edge/users?filter[self]=true&fields[users]=id,name")
    suspend fun retrieveUser(): Result<UserResponseDto>
}
