package com.chesire.nekomp.library.datasource.favorite

import com.chesire.nekomp.core.network.ResultConverterFactory
import com.chesire.nekomp.core.network.plugin.installAuth
import com.chesire.nekomp.core.network.plugin.installContentNegotiation
import com.chesire.nekomp.core.network.plugin.installLogging
import com.chesire.nekomp.library.datasource.auth.AuthRepository
import com.chesire.nekomp.library.datasource.favorite.local.FavoriteStorage
import com.chesire.nekomp.library.datasource.favorite.remote.FavoriteApi
import com.chesire.nekomp.library.datasource.favorite.remote.createFavoriteApi
import de.jensklingenberg.ktorfit.ktorfitBuilder
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.providers.BearerTokens
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val libraryFavoriteModule = module {
    factory<FavoriteApi> {
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
        }.build().createFavoriteApi()
    }
    singleOf(::FavoriteRepository)
    singleOf(::FavoriteStorage)
}
