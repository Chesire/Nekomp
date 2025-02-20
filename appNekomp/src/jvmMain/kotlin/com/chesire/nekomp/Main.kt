package com.chesire.nekomp

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.chesire.nekomp.library.datasource.trending.TrendingRepository
import org.koin.compose.getKoin

fun main() {
    initKoin { }
    application {
        val trending = getKoin().get<TrendingRepository>()
        LaunchedEffect(Unit) {
            // Perform full sync of trending on start
            trending.performFullSync()
        }
        Window(
            onCloseRequest = ::exitApplication,
            title = "Nekomp",
        ) {
            App()
        }
    }
}
