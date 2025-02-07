package com.chesire.nekomp.library.datasource.auth

import com.chesire.nekomp.core.network.EitherConverterFactory
import com.chesire.nekomp.library.datasource.auth.api.AuthApi
import com.chesire.nekomp.library.datasource.auth.api.createAuthApi
import de.jensklingenberg.ktorfit.ktorfitBuilder
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val libraryAuthModule = module {
    factory<AuthApi> {
        ktorfitBuilder {
            baseUrl("https://kitsu.io/")
            httpClient(
                client = HttpClient {
                    install(ContentNegotiation) {
                        json(
                            Json {
                                isLenient = true
                                ignoreUnknownKeys = true
                            }
                        )
                    }
                }
            )
            converterFactories(EitherConverterFactory())
        }.build().createAuthApi()
    }
}
