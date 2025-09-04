package com.chesire.nekomp.core.network.plugin

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.plugin
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode

/**
 * [refreshTokens] is a lambda that if true means that the tokens could not be refreshed so a logout
 * will occur.
 * [onRefreshError] is called when tokens cannot be refreshed and logout should occur.
 */
fun HttpClient.installAuth(
    getTokens: suspend () -> String?, // Return just the access token string
    refreshTokens: suspend () -> Boolean, // Returns true if refresh failed
    onRefreshError: suspend () -> Unit
) {
    plugin(HttpSend).intercept { request ->
        // Add token to every request
        val token = getTokens()
        if (token != null) {
            request.header(HttpHeaders.Authorization, "Bearer $token")
        }

        // Execute the request
        val originalCall = execute(request)

        // Check if we need to refresh tokens
        if (originalCall.response.status == HttpStatusCode.Unauthorized ||
            originalCall.response.status == HttpStatusCode.Forbidden
        ) {
            Logger.i("HttpClient") { "Auth failed, attempting token refresh" }
            val refreshFailed = refreshTokens()

            if (refreshFailed) {
                Logger.e("HttpClient") { "Token refresh failed, executing logout" }
                onRefreshError()
                error("Could not refresh tokens, logout executed")
            } else {
                Logger.i("HttpClient") { "Token refresh successful, retrying request" }
                // Get fresh token and retry
                val newToken = getTokens()
                if (newToken != null) {
                    request.headers.remove(HttpHeaders.Authorization)
                    request.header(HttpHeaders.Authorization, "Bearer $newToken")
                    return@intercept execute(request)
                }
            }
        }

        originalCall
    }
}
