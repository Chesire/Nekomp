package com.chesire.nekomp.library.datasource.airing

import com.chesire.nekomp.core.network.ResultConverterFactory
import com.chesire.nekomp.core.network.plugin.installContentNegotiation
import com.chesire.nekomp.core.network.plugin.installLogging
import com.chesire.nekomp.library.datasource.airing.local.AiringStorage
import com.chesire.nekomp.library.datasource.airing.remote.AiringApi
import com.chesire.nekomp.library.datasource.airing.remote.createAiringApi
import de.jensklingenberg.ktorfit.ktorfitBuilder
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val libraryAiringModule = module {
    factory<AiringApi> {
        ktorfitBuilder {
            baseUrl("https://api.jikan.moe/")
            httpClient(
                client = HttpClient {
                    installContentNegotiation()
                    installLogging()
                }
            )
            converterFactories(ResultConverterFactory())
        }.build().createAiringApi()
    }
    singleOf(::AiringStorage)
    singleOf(::AiringRepository)
}
