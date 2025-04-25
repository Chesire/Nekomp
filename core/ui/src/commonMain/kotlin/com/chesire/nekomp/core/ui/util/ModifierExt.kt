package com.chesire.nekomp.core.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

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
