package com.chesire.nekomp.api

import com.chesire.nekomp.model.LoginRequestDto
import com.chesire.nekomp.model.LoginResponseDto
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST

interface AuthApi {

    @POST("api/oauth/token")
    suspend fun login(@Body body: LoginRequestDto): Response<LoginResponseDto>
}
