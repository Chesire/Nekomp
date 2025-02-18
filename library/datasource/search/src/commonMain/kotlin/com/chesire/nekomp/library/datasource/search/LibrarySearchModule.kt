package com.chesire.nekomp.library.datasource.search

import co.touchlab.kermit.Logger
import com.chesire.nekomp.core.network.ResultConverterFactory
import com.chesire.nekomp.core.network.plugin.installContentNegotiation
import com.chesire.nekomp.core.network.plugin.installLogging
import com.chesire.nekomp.library.datasource.auth.AuthRepository
import com.chesire.nekomp.library.datasource.search.remote.SearchApi
import com.chesire.nekomp.library.datasource.search.remote.createSearchApi
import de.jensklingenberg.ktorfit.ktorfitBuilder
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.http.HttpStatusCode
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val librarySearchModule = module {
    factory<SearchApi> {
        ktorfitBuilder {
            baseUrl("https://kitsu.io/")
            httpClient(
                client = HttpClient {
                    installContentNegotiation()
                    installLogging()
                    install(Auth) {
                        reAuthorizeOnResponse { it.status == HttpStatusCode.Forbidden }
                        bearer {
                            loadTokens {
                                val authRepository = get<AuthRepository>()
                                val access = authRepository.accessToken() ?: ""
                                BearerTokens(
                                    accessToken = access,
                                    refreshToken = authRepository.refreshToken()
                                )
                            }

                            refreshTokens {
                                Logger.i("LibrarySearchModule") { "Refreshing auth tokens" }
                                val authRepository = get<AuthRepository>()
                                authRepository.refresh()
                                BearerTokens(
                                    accessToken = authRepository.accessToken() ?: "",
                                    refreshToken = authRepository.refreshToken()
                                )
                            }
                        }
                    }
                }
            )
            converterFactories(ResultConverterFactory())
        }.build().createSearchApi()
    }
    singleOf(::SearchService)
}
