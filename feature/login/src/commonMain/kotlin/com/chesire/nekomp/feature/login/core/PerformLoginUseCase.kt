package com.chesire.nekomp.feature.login.core

import com.chesire.nekomp.core.network.Either
import com.chesire.nekomp.library.datasource.auth.api.AuthApi
import com.chesire.nekomp.library.datasource.auth.model.LoginRequestDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class PerformLoginUseCase(private val authApi: AuthApi) {

    suspend operator fun invoke(username: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            val result = authApi.login(LoginRequestDto(username, password, "password"))
            result is Either.Success
        }
    }
}
