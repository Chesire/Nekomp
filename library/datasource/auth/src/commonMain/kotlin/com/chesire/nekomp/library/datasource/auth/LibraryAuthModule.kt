package com.chesire.nekomp.library.datasource.auth

import com.chesire.nekomp.core.network.ResultConverterFactory
import com.chesire.nekomp.core.network.plugin.installContentNegotiation
import com.chesire.nekomp.core.preferences.createDataStore
import com.chesire.nekomp.library.datasource.auth.local.AuthStorage
import com.chesire.nekomp.library.datasource.auth.remote.AuthApi
import com.chesire.nekomp.library.datasource.auth.remote.createAuthApi
import de.jensklingenberg.ktorfit.ktorfitBuilder
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

private const val AUTH_DATASTORE_NAME = "nekomp.auth.preferences_pb"

val libraryAuthModule = module {
    factory<AuthApi> {
        ktorfitBuilder {
            baseUrl("https://kitsu.io/")
            httpClient(
                client = HttpClient {
                    installContentNegotiation()
                }
            )
            converterFactories(ResultConverterFactory())
        }.build().createAuthApi()
    }
    singleOf(::AuthRepository)
    single<AuthStorage> {
        AuthStorage(createDataStore(AUTH_DATASTORE_NAME))
    }
}
