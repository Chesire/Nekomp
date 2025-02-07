package com.chesire.nekomp.library.datasource.auth

import com.chesire.nekomp.library.datasource.auth.remote.AuthApi
import com.chesire.nekomp.library.datasource.auth.remote.model.LoginRequestDto

class AuthRepository(
    private val authApi: AuthApi,
    // local storage
) {
    var accessToken: String
        get() = "" // retrieve
        private set(token) {
            // set
        }

    var refreshToken: String
        get() = ""
        private set(token) {
            // set
        }

    suspend fun authenticate(username: String, password: String): Result<String> {
        return authApi
            .login(LoginRequestDto(username, password, "password"))
            .onSuccess {
                accessToken = it.accessToken
                refreshToken = it.refreshToken
            }
            .map { it.accessToken }
    }
}
