package com.chesire.nekomp.library.datasource.auth.remote

import com.chesire.nekomp.library.datasource.auth.remote.model.LoginRequestDto
import com.chesire.nekomp.library.datasource.auth.remote.model.LoginResponseDto
import com.chesire.nekomp.library.datasource.auth.remote.model.RefreshRequestDto
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST

interface AuthApi {

    @Headers("Content-Type: application/json")
    @POST("api/oauth/token")
    suspend fun login(@Body body: LoginRequestDto): Result<LoginResponseDto>

    @Headers("Content-Type: application/json")
    @POST("api/oauth/token")
    suspend fun refresh(@Body body: RefreshRequestDto): Result<LoginResponseDto>
}
