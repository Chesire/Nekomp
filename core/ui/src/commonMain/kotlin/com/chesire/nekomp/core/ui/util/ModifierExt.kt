package com.chesire.nekomp.core.ui.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun Modifier.ifTrue(
    condition: Boolean,
    modifier: @Composable Modifier.() -> Modifier
): Modifier {
    return if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }
}

@Composable
fun Modifier.ifFalse(
    condition: Boolean,
    modifier: @Composable Modifier.() -> Modifier
): Modifier {
    return if (!condition) {
        then(modifier(Modifier))
    } else {
        this
    }
}

/**
 * Adds a background which makes part of the view transparent.
 */
@Composable
fun Modifier.transparentBacking(): Modifier {
    return this
        .fillMaxSize()
        .background(
            brush = Brush.verticalGradient(
                .5F to Color.Transparent,
                .7f to MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                .8f to MaterialTheme.colorScheme.background.copy(alpha = 0.9f),
                1F to MaterialTheme.colorScheme.background.copy(alpha = 1f)
            )
        )
}
