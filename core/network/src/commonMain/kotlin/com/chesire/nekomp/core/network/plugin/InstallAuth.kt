package com.chesire.nekomp.core.network.plugin

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.http.HttpStatusCode

/**
 * [refreshTokens] is a lambda that if true means that the tokens could not be refreshed so a logout
 * will occur.
 * [onRefreshError] is called when tokens cannot be refreshed and logout should occur.
 */
fun HttpClientConfig<*>.installAuth(
    getTokens: suspend () -> BearerTokens,
    refreshTokens: suspend () -> Boolean,
    onRefreshError: suspend () -> Unit
) {
    install(Auth) {
        reAuthorizeOnResponse { response ->
            response.status == HttpStatusCode.Forbidden ||
                response.status == HttpStatusCode.Unauthorized
        }
        bearer {
            loadTokens {
                getTokens()
            }

            refreshTokens {
                Logger.i("HttpClient") { "Refreshing auth tokens" }
                val invalidTokens = refreshTokens()
                if (invalidTokens) {
                    Logger.e("HttpClient") { "Token refresh failed, executing logout" }
                    onRefreshError()
                    error("Could not refresh tokens, logout executed")
                } else {
                    getTokens()
                }
            }
        }
    }
}
