package com.chesire.nekomp.core.network.plugin

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun HttpClientConfig<*>.installContentNegotiation() {
    install(ContentNegotiation) {
        json(
            Json {
                isLenient = true
                ignoreUnknownKeys = true
                explicitNulls = false
            }
        )
    }
}
