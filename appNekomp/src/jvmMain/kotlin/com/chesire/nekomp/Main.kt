package com.chesire.nekomp

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.chesire.nekomp.library.datasource.airing.AiringRepository
import com.chesire.nekomp.library.datasource.mapping.MappingRepository
import com.chesire.nekomp.library.datasource.trending.TrendingRepository
import org.koin.compose.koinInject

fun main() {
    initKoin { }
    application {
        val initializers = koinInject<Initializers>()
        val airing = koinInject<AiringRepository>()
        val mapping = koinInject<MappingRepository>()
        val trending = koinInject<TrendingRepository>()
        LaunchedEffect(Unit) {
            // Build up initial DB files
            initializers.prepopulateDb()
            // Perform full sync of airing data on start
            airing.syncCurrentAiring()
            // Fire off updating the mappings
            mapping.updateMappings()
        }
        LaunchedEffect(Unit) {
            // Perform full sync of trending on start
            trending.performFullSync()
        }
        Window(
            onCloseRequest = ::exitApplication,
            title = "Nekomp"
        ) {
            App()
        }
    }
}
