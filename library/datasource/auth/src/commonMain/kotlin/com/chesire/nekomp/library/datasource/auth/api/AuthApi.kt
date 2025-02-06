package com.chesire.nekomp.library.datasource.auth.api

import com.chesire.nekomp.core.network.Either
import com.chesire.nekomp.library.datasource.auth.model.LoginRequestDto
import com.chesire.nekomp.library.datasource.auth.model.LoginResponseDto
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST

interface AuthApi {

    @Headers("Content-Type: application/json")
    @POST("api/oauth/token")
    suspend fun login(@Body body: LoginRequestDto): Either<LoginResponseDto>
}
