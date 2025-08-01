package com.chesire.nekomp.library.datasource.library

import com.chesire.nekomp.core.network.RefreshErrorExecutor
import com.chesire.nekomp.core.network.ResultConverterFactory
import com.chesire.nekomp.core.network.plugin.installAuth
import com.chesire.nekomp.core.network.plugin.installContentNegotiation
import com.chesire.nekomp.core.network.plugin.installLogging
import com.chesire.nekomp.library.datasource.auth.AuthFailure
import com.chesire.nekomp.library.datasource.auth.AuthRepository
import com.chesire.nekomp.library.datasource.library.local.LibraryStorage
import com.chesire.nekomp.library.datasource.library.remote.LibraryApi
import com.chesire.nekomp.library.datasource.library.remote.createLibraryApi
import de.jensklingenberg.ktorfit.ktorfitBuilder
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.providers.BearerTokens
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val libraryLibraryModule = module {
    factory<LibraryApi> {
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
                            val result = get<AuthRepository>().refresh()
                            result.error is AuthFailure.BadToken
                        },
                        onRefreshError = {
                            get<RefreshErrorExecutor>().invoke()
                        }
                    )
                }
            )
            converterFactories(ResultConverterFactory())
        }.build().createLibraryApi()
    }
    singleOf(::LibraryRepository)
    singleOf(::LibraryStorage)
}
