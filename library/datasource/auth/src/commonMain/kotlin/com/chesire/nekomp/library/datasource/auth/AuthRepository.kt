package com.chesire.nekomp.library.datasource.auth

import co.touchlab.kermit.Logger
import com.chesire.nekomp.core.network.NetworkError
import com.chesire.nekomp.library.datasource.auth.local.AuthStorage
import com.chesire.nekomp.library.datasource.auth.remote.AuthApi
import com.chesire.nekomp.library.datasource.auth.remote.model.GRANT_TYPE_PASSWORD
import com.chesire.nekomp.library.datasource.auth.remote.model.GRANT_TYPE_REFRESH
import com.chesire.nekomp.library.datasource.auth.remote.model.LoginRequestDto
import com.chesire.nekomp.library.datasource.auth.remote.model.RefreshRequestDto
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking

// TODO: Add a remote data source to handle all this parsing etc
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

    suspend fun authenticate(username: String, password: String): Result<String, AuthFailure> {
        return authApi
            .login(LoginRequestDto(username, password, GRANT_TYPE_PASSWORD))
            .onSuccess { updateTokens(it.accessToken, it.refreshToken) }
            .fold(
                onSuccess = { Ok(it.accessToken) },
                onFailure = {
                    val error = it as NetworkError
                    val type = when (error) {
                        is NetworkError.Api -> when (error.code) {
                            HttpStatusCode.Unauthorized.value -> AuthFailure.InvalidCredentials
                            else -> AuthFailure.BadRequest
                        }

                        is NetworkError.Generic -> AuthFailure.BadRequest
                    }
                    Err(type)
                }
            )
    }

    suspend fun refresh(): Result<String, AuthFailure> {
        return authApi
            .refresh(RefreshRequestDto(refreshToken() ?: "", GRANT_TYPE_REFRESH))
            .onSuccess { updateTokens(it.accessToken, it.refreshToken) }
            .fold(
                onSuccess = { Ok(it.accessToken) },
                onFailure = {
                    val error = it as NetworkError
                    val type = when (error) {
                        is NetworkError.Api -> when (error.code) {
                            HttpStatusCode.Unauthorized.value -> AuthFailure.InvalidCredentials
                            else -> AuthFailure.BadRequest
                        }

                        is NetworkError.Generic -> AuthFailure.BadRequest
                    }
                    Err(type)
                }
            )
    }

    suspend fun clear() {
        Logger.d("AuthRepository") { "Clearing auth tokens" }
        authStorage.clear()
    }
}

sealed interface AuthFailure {
    data object InvalidCredentials : AuthFailure
    data object BadRequest : AuthFailure
}
