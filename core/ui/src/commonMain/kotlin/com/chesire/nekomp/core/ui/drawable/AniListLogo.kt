package com.chesire.nekomp.core.ui.drawable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val AniListLogo: ImageVector
    @Composable
    get() {
        val color = Color(0xFF000000)
        _Anilist = remember {
            ImageVector.Builder(
                name = "Anilist",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f
            ).apply {
                path(fill = SolidColor(color)) {
                    moveTo(24f, 17.53f)
                    verticalLineToRelative(2.421f)
                    curveToRelative(0f, 0.71f, -0.391f, 1.101f, -1.1f, 1.101f)
                    horizontalLineToRelative(-5f)
                    lineToRelative(-0.057f, -0.165f)
                    lineTo(11.84f, 3.736f)
                    curveToRelative(0.106f, -0.502f, 0.46f, -0.788f, 1.053f, -0.788f)
                    horizontalLineToRelative(2.422f)
                    curveToRelative(0.71f, 0f, 1.1f, 0.391f, 1.1f, 1.1f)
                    verticalLineToRelative(12.38f)
                    lineTo(22.9f, 16.428f)
                    curveToRelative(0.71f, 0f, 1.1f, 0.392f, 1.1f, 1.101f)
                    close()
                    moveTo(11.034f, 2.947f)
                    lineToRelative(6.337f, 18.104f)
                    horizontalLineToRelative(-4.918f)
                    lineToRelative(-1.052f, -3.131f)
                    lineTo(6.019f, 17.92f)
                    lineToRelative(-1.077f, 3.131f)
                    lineTo(0f, 21.051f)
                    lineTo(6.361f, 2.948f)
                    horizontalLineToRelative(4.673f)
                    close()
                    moveTo(10.374f, 13.907f)
                    lineTo(8.684f, 8.893f)
                    lineTo(7.143f, 13.908f)
                    horizontalLineToRelative(3.23f)
                    close()
                }
            }.build()
        }

        return _Anilist!!
    }

@Suppress("ObjectPropertyName")
private var _Anilist: ImageVector? = null
