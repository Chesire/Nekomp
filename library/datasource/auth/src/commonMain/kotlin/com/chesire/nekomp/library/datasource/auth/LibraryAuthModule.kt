package com.chesire.nekomp.library.datasource.auth

import com.chesire.nekomp.core.network.ResultConverterFactory
import com.chesire.nekomp.library.datasource.auth.local.AuthStorage
import com.chesire.nekomp.library.datasource.auth.local.createDataStore
import com.chesire.nekomp.library.datasource.auth.remote.AuthApi
import com.chesire.nekomp.library.datasource.auth.remote.createAuthApi
import de.jensklingenberg.ktorfit.ktorfitBuilder
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
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
            converterFactories(ResultConverterFactory())
        }.build().createAuthApi()
    }
    singleOf(::AuthRepository)
    single<AuthStorage> {
        AuthStorage(createDataStore())
    }
}
