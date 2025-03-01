package com.chesire.nekomp.core.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.chesire.nekomp.core.ui.theme.darkTheme
import com.chesire.nekomp.core.ui.theme.lightTheme

@Composable
fun NekompTheme(
    useDarkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colors = if (useDarkTheme) darkTheme else lightTheme
    val shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(4.dp),
        large = RoundedCornerShape(0.dp)
    )

    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}
