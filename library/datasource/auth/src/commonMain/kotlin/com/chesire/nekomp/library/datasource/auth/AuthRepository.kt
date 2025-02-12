package com.chesire.nekomp.library.datasource.auth

import com.chesire.nekomp.library.datasource.auth.local.AuthStorage
import com.chesire.nekomp.library.datasource.auth.remote.AuthApi
import com.chesire.nekomp.library.datasource.auth.remote.model.LoginRequestDto
import kotlinx.coroutines.runBlocking

class AuthRepository(
    private val authApi: AuthApi,
    private val authStorage: AuthStorage
) {

    suspend fun accessToken(): String? = authStorage.acquireAccessToken()
    fun accessTokenSync(): String? = runBlocking { authStorage.acquireAccessToken() } // This will do for now
    suspend fun refreshToken(): String? = authStorage.acquireRefreshToken()

    private suspend fun updateTokens(accessToken: String, refreshToken: String) {
        authStorage.apply {
            updateAccessToken(accessToken)
            updateRefreshToken(refreshToken)
        }
    }

    suspend fun authenticate(username: String, password: String): Result<String> {
        return authApi
            .login(LoginRequestDto(username, password, "password"))
            .onSuccess { updateTokens(it.accessToken, it.refreshToken) }
            .map { it.accessToken }
    }
}
