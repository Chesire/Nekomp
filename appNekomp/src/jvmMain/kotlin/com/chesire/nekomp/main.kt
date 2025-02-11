package com.chesire.nekomp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.chesire.nekomp.di.initKoin

fun main() = application {
    initKoin { }
    Window(
        onCloseRequest = ::exitApplication,
        title = "KotlinProject",
    ) {
        App()
    }
}
