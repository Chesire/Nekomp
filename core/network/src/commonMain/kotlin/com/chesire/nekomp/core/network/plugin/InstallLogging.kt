package com.chesire.nekomp.core.network.plugin

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging

fun HttpClientConfig<*>.installLogging() {
    install(Logging) {
        logger = object : io.ktor.client.plugins.logging.Logger {
            override fun log(message: String) {
                Logger.v("HttpClient") { message }
            }
        }
        level = LogLevel.ALL
    }
}
