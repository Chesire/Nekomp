package com.chesire.nekomp.core.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.unit.dp
import com.chesire.nekomp.core.ui.theme.LocalExtendedColors
import com.chesire.nekomp.core.ui.theme.NekompExtendedColorScheme
import com.chesire.nekomp.core.ui.theme.NekompExtendedDarkColors
import com.chesire.nekomp.core.ui.theme.NekompExtendedLightColors
import com.chesire.nekomp.core.ui.theme.getColorScheme

@Composable
fun NekompTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val extendedColors = if (useDarkTheme) NekompExtendedDarkColors else NekompExtendedLightColors
    val colorScheme = getColorScheme(useDarkTheme, dynamicColor)
    val shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(4.dp),
        large = RoundedCornerShape(0.dp)
    )

    CompositionLocalProvider(
        LocalExtendedColors provides extendedColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            shapes = shapes,
            content = content
        )
    }
}

object NekompTheme {

    val colors: NekompExtendedColorScheme
        @Composable @ReadOnlyComposable get() = LocalExtendedColors.current
}
