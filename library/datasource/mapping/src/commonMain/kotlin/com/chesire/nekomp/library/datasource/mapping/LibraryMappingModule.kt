package com.chesire.nekomp.library.datasource.mapping

import com.chesire.nekomp.core.network.plugin.installContentNegotiation
import com.chesire.nekomp.core.network.plugin.installLogging
import com.chesire.nekomp.library.datasource.mapping.local.MappingLocalDataSource
import com.chesire.nekomp.library.datasource.mapping.remote.MappingRemoteDataSource
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val libraryMappingModule = module {
    singleOf(::MappingRepository)
    factoryOf(::MappingLocalDataSource)
    factory<MappingRemoteDataSource> {
        MappingRemoteDataSource(
            httpClient = HttpClient {
                installContentNegotiation()
                installLogging()
            },
            json = Json { explicitNulls = false }
        )
    }
}
