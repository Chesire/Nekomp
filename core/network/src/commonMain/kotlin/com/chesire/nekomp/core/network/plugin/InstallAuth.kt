package com.chesire.nekomp.core.network.plugin

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.http.HttpStatusCode

fun HttpClientConfig<*>.installAuth(
    getTokens: suspend () -> BearerTokens,
    refreshTokens: suspend () -> Unit
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
                refreshTokens()
                getTokens()
            }
        }
    }
}
