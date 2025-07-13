package com.chesire.nekomp.core.ui.component.series

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlusOne
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.chesire.nekomp.core.resources.NekoRes
import com.chesire.nekomp.core.ui.NekompTheme
import com.chesire.nekomp.core.ui.theme.Values
import nekomp.core.resources.generated.resources.grid_item_plus_one
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SeriesGridItem(
    title: String,
    backgroundImage: String,
    progress: String,
    progressPercent: Float,
    isUpdating: Boolean,
    modifier: Modifier = Modifier,
    showPlusButton: Boolean = true,
    onClick: () -> Unit,
    onPlusClick: () -> Unit
) {
    val progressBarValue by animateFloatAsState(
        targetValue = progressPercent,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )
    Column(
        modifier = modifier.width(IntrinsicSize.Min),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            onClick = onClick,
            modifier = Modifier.size(width = Values.GridItemWidth, height = Values.GridItemHeight),
        ) {
            Box {
                AsyncImage(
                    model = backgroundImage,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                .5F to Color.Transparent,
                                .7f to MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                                .8f to MaterialTheme.colorScheme.background.copy(alpha = 0.9f),
                                1F to MaterialTheme.colorScheme.background.copy(alpha = 1f)
                            )
                        )
                )
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(
                            text = progress,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.titleSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (showPlusButton) {
                            IconButton(
                                onClick = onPlusClick,
                                enabled = !isUpdating
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlusOne,
                                    contentDescription = stringResource(NekoRes.string.grid_item_plus_one)
                                )
                            }
                        }
                    }
                    LinearProgressIndicator(
                        progress = { progressBarValue },
                        modifier = Modifier.height(4.dp),
                        gapSize = 0.dp,
                        drawStopIndicator = {}
                    )
                }
            }
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            minLines = 2,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
private fun DefaultSeriesGridItemPreview() {
    NekompTheme {
        SeriesGridItem(
            title = "Title",
            backgroundImage = "",
            progress = "6 / 12",
            progressPercent = 0.5f,
            isUpdating = false,
            modifier = Modifier,
            showPlusButton = true,
            onClick = {},
            onPlusClick = {}
        )
    }
}

@Preview
@Composable
private fun UpdatingSeriesGridItemPreview() {
    NekompTheme {
        SeriesGridItem(
            title = "Title",
            backgroundImage = "",
            progress = "1 / 12",
            progressPercent = 0.1f,
            isUpdating = true,
            modifier = Modifier,
            showPlusButton = true,
            onClick = {},
            onPlusClick = {}
        )
    }
}
