package com.chesire.nekomp.library.datasource.user

import com.chesire.nekomp.core.network.RefreshErrorExecutor
import com.chesire.nekomp.core.network.ResultConverterFactory
import com.chesire.nekomp.core.network.plugin.installAuth
import com.chesire.nekomp.core.network.plugin.installContentNegotiation
import com.chesire.nekomp.core.network.plugin.installLogging
import com.chesire.nekomp.library.datasource.auth.AuthFailure
import com.chesire.nekomp.library.datasource.auth.AuthRepository
import com.chesire.nekomp.library.datasource.user.local.UserLocalDataSource
import com.chesire.nekomp.library.datasource.user.local.UserStorage
import com.chesire.nekomp.library.datasource.user.remote.UserApi
import com.chesire.nekomp.library.datasource.user.remote.createUserApi
import de.jensklingenberg.ktorfit.ktorfitBuilder
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.providers.BearerTokens
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val libraryUserModule = module {
    factory<UserApi> {
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
        }.build().createUserApi()
    }
    singleOf(::UserRepository)
    singleOf(::UserLocalDataSource).bind(UserStorage::class)
}
