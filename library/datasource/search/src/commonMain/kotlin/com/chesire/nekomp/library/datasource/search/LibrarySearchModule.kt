package com.chesire.nekomp.library.datasource.search

import com.chesire.nekomp.core.network.ResultConverterFactory
import com.chesire.nekomp.core.network.plugin.installAuth
import com.chesire.nekomp.core.network.plugin.installContentNegotiation
import com.chesire.nekomp.core.network.plugin.installLogging
import com.chesire.nekomp.library.datasource.auth.AuthRepository
import com.chesire.nekomp.library.datasource.search.remote.SearchApi
import com.chesire.nekomp.library.datasource.search.remote.createSearchApi
import de.jensklingenberg.ktorfit.ktorfitBuilder
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.providers.BearerTokens
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
                    installAuth(
                        getTokens = {
                            val authRepository = get<AuthRepository>()
                            BearerTokens(
                                accessToken = authRepository.accessToken() ?: "",
                                refreshToken = authRepository.refreshToken()
                            )
                        },
                        refreshTokens = {
                            get<AuthRepository>().refresh()
                        }
                    )
                }
            )
            converterFactories(ResultConverterFactory())
        }.build().createSearchApi()
    }
    singleOf(::SearchService)
}
