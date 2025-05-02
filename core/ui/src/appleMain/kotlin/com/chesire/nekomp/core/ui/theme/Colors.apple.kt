package com.chesire.nekomp.core.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
actual fun getColorScheme(
    darkTheme: Boolean,
    dynamicColor: Boolean
): ColorScheme {
    return when {
        darkTheme -> darkScheme
        else -> lightScheme
    }
}
