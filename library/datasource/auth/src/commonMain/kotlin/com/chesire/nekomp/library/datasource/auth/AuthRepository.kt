package com.chesire.nekomp.library.datasource.auth

import com.chesire.nekomp.library.datasource.auth.local.AuthStorage
import com.chesire.nekomp.library.datasource.auth.remote.AuthApi
import com.chesire.nekomp.library.datasource.auth.remote.model.GRANT_TYPE_PASSWORD
import com.chesire.nekomp.library.datasource.auth.remote.model.GRANT_TYPE_REFRESH
import com.chesire.nekomp.library.datasource.auth.remote.model.LoginRequestDto
import com.chesire.nekomp.library.datasource.auth.remote.model.RefreshRequestDto
import kotlinx.coroutines.runBlocking

class AuthRepository(
    private val authApi: AuthApi,
    private val authStorage: AuthStorage
) {

    // This will do for now
    fun accessTokenSync(): String? = runBlocking { accessToken() }
    suspend fun accessToken(): String? = authStorage.acquireAccessToken()
    suspend fun refreshToken(): String? = authStorage.acquireRefreshToken()

    private suspend fun updateTokens(accessToken: String, refreshToken: String) {
        authStorage.apply {
            updateAccessToken(accessToken)
            updateRefreshToken(refreshToken)
        }
    }

    suspend fun authenticate(username: String, password: String): Result<String> {
        return authApi
            .login(LoginRequestDto(username, password, GRANT_TYPE_PASSWORD))
            .onSuccess { updateTokens(it.accessToken, it.refreshToken) }
            .map { it.accessToken }
    }

    suspend fun refresh(): Result<String> {
        return authApi
            .refresh(RefreshRequestDto(refreshToken() ?: "", GRANT_TYPE_REFRESH))
            .onSuccess { updateTokens(it.accessToken, it.refreshToken) }
            .map { it.accessToken }
    }
}
