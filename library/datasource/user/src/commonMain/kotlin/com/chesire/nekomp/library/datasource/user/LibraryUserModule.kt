package com.chesire.nekomp.library.datasource.user

import com.chesire.nekomp.core.network.ResultConverterFactory
import com.chesire.nekomp.library.datasource.auth.AuthRepository
import com.chesire.nekomp.library.datasource.user.local.UserStorage
import com.chesire.nekomp.library.datasource.user.remote.UserApi
import com.chesire.nekomp.library.datasource.user.remote.createUserApi
import de.jensklingenberg.ktorfit.ktorfitBuilder
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val libraryUserModule = module {
    factory<UserApi> {
        ktorfitBuilder {
            baseUrl("https://kitsu.io/")
            httpClient(
                client = HttpClient {
                    install(ContentNegotiation) {
                        json(
                            Json {
                                isLenient = true
                                ignoreUnknownKeys = true
                                explicitNulls = false
                            }
                        )
                    }
                    install(Auth) {
                        bearer {
                            loadTokens {
                                val authRepository = get<AuthRepository>()
                                BearerTokens(
                                    accessToken = authRepository.accessToken() ?: "",
                                    refreshToken = authRepository.refreshToken()
                                )
                            }

                            // Need to support multiple errors https://github.com/ktorio/ktor/pull/4420
                            // refreshTokens {
                            //     val authRepository = get<AuthRepository>()
                            //     // make call to refresh the token
                            //
                            //     BearerTokens(
                            //         accessToken = authRepository.accessToken() ?: "",
                            //         refreshToken = authRepository.refreshToken()
                            //     )
                            // }
                        }
                    }
                }
            )
            converterFactories(ResultConverterFactory())
        }.build().createUserApi()
    }
    singleOf(::UserRepository)
    singleOf(::UserStorage)
}
