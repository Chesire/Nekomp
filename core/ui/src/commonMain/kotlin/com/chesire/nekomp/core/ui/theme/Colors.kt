package com.chesire.nekomp.core.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Theming from https://material-foundation.github.io/material-theme-builder/

val primaryLight = Color(0xFF455E91)
val onPrimaryLight = Color(0xFFFFFFFF)
val primaryContainerLight = Color(0xFFD8E2FF)
val onPrimaryContainerLight = Color(0xFF2C4678)
val secondaryLight = Color(0xFF575E71)
val onSecondaryLight = Color(0xFFFFFFFF)
val secondaryContainerLight = Color(0xFFDBE2F9)
val onSecondaryContainerLight = Color(0xFF3F4759)
val tertiaryLight = Color(0xFF715573)
val onTertiaryLight = Color(0xFFFFFFFF)
val tertiaryContainerLight = Color(0xFFFCD7FB)
val onTertiaryContainerLight = Color(0xFF583E5A)
val errorLight = Color(0xFFBA1A1A)
val onErrorLight = Color(0xFFFFFFFF)
val errorContainerLight = Color(0xFFFFDAD6)
val onErrorContainerLight = Color(0xFF93000A)
val backgroundLight = Color(0xFFFAF9FF)
val onBackgroundLight = Color(0xFF1A1B20)
val surfaceLight = Color(0xFFFAF9FF)
val onSurfaceLight = Color(0xFF1A1B20)
val surfaceVariantLight = Color(0xFFE1E2EC)
val onSurfaceVariantLight = Color(0xFF44474F)
val outlineLight = Color(0xFF757780)
val outlineVariantLight = Color(0xFFC5C6D0)
val scrimLight = Color(0xFF000000)
val inverseSurfaceLight = Color(0xFF2F3036)
val inverseOnSurfaceLight = Color(0xFFF1F0F7)
val inversePrimaryLight = Color(0xFFAEC6FF)
val surfaceDimLight = Color(0xFFDAD9E0)
val surfaceBrightLight = Color(0xFFFAF9FF)
val surfaceContainerLowestLight = Color(0xFFFFFFFF)
val surfaceContainerLowLight = Color(0xFFF3F3FA)
val surfaceContainerLight = Color(0xFFEEEDF4)
val surfaceContainerHighLight = Color(0xFFE8E7EF)
val surfaceContainerHighestLight = Color(0xFFE2E2E9)

val primaryDark = Color(0xFFAEC6FF)
val onPrimaryDark = Color(0xFF122F60)
val primaryContainerDark = Color(0xFF2C4678)
val onPrimaryContainerDark = Color(0xFFD8E2FF)
val secondaryDark = Color(0xFFBFC6DC)
val onSecondaryDark = Color(0xFF293041)
val secondaryContainerDark = Color(0xFF3F4759)
val onSecondaryContainerDark = Color(0xFFDBE2F9)
val tertiaryDark = Color(0xFFDFBBDE)
val onTertiaryDark = Color(0xFF402843)
val tertiaryContainerDark = Color(0xFF583E5A)
val onTertiaryContainerDark = Color(0xFFFCD7FB)
val errorDark = Color(0xFFFFB4AB)
val onErrorDark = Color(0xFF690005)
val errorContainerDark = Color(0xFF93000A)
val onErrorContainerDark = Color(0xFFFFDAD6)
val backgroundDark = Color(0xFF121318)
val onBackgroundDark = Color(0xFFE2E2E9)
val surfaceDark = Color(0xFF121318)
val onSurfaceDark = Color(0xFFE2E2E9)
val surfaceVariantDark = Color(0xFF44474F)
val onSurfaceVariantDark = Color(0xFFC5C6D0)
val outlineDark = Color(0xFF8E9099)
val outlineVariantDark = Color(0xFF44474F)
val scrimDark = Color(0xFF000000)
val inverseSurfaceDark = Color(0xFFE2E2E9)
val inverseOnSurfaceDark = Color(0xFF2F3036)
val inversePrimaryDark = Color(0xFF455E91)
val surfaceDimDark = Color(0xFF121318)
val surfaceBrightDark = Color(0xFF38393E)
val surfaceContainerLowestDark = Color(0xFF0C0E13)
val surfaceContainerLowDark = Color(0xFF1A1B20)
val surfaceContainerDark = Color(0xFF1E1F25)
val surfaceContainerHighDark = Color(0xFF282A2F)
val surfaceContainerHighestDark = Color(0xFF33353A)

val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

@Immutable
interface NekompExtendedColorScheme {
    val red: Color
    val green: Color
    val blue: Color
    val yellow: Color
}

@Immutable
internal object NekompExtendedLightColors : NekompExtendedColorScheme {
    override val red = Color(0xFFFFCDD2)
    override val green = Color(0xFFB2DFDB)
    override val blue = Color(0xFFBBDEFB)
    override val yellow = Color(0xFFFFECB3)
}

@Immutable
internal object NekompExtendedDarkColors : NekompExtendedColorScheme {
    override val red = Color(0xFFD32F2F)
    override val green = Color(0xFF00796B)
    override val blue = Color(0xFF1976D2)
    override val yellow = Color(0xFFBA8E23)
}

internal val LocalExtendedColors = staticCompositionLocalOf<NekompExtendedColorScheme> {
    object : NekompExtendedColorScheme {
        override val red: Color = Color.Unspecified
        override val green: Color = Color.Unspecified
        override val blue: Color = Color.Unspecified
        override val yellow: Color = Color.Unspecified
    }
}

@Composable
expect fun getColorScheme(darkTheme: Boolean, dynamicColor: Boolean): ColorScheme
