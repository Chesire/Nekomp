package com.chesire.nekomp.core.ui.component.series

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlusOne
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.chesire.nekomp.core.resources.NekoRes
import nekomp.core.resources.generated.resources.grid_item_plus_one
import org.jetbrains.compose.resources.stringResource

@Composable
fun SeriesGridItem(
    title: String,
    backgroundImage: String,
    progress: Float,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onPlusClick: () -> Unit
) {
    var width by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    Column(
        modifier = modifier.width(IntrinsicSize.Min),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            onClick = { onClick() },
            modifier = Modifier.wrapContentSize(),
        ) {
            Box(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .width(IntrinsicSize.Min)
            ) {
                AsyncImage(
                    model = backgroundImage,
                    contentDescription = null,
                    modifier = Modifier.fillMaxHeight(),
                    onState = {
                        width = with(density) {
                            it.painter?.intrinsicSize?.width?.toInt()?.toDp() ?: 0.dp
                        }
                    }
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
                    modifier = Modifier.fillMaxHeight().width(width),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedIconButton(onClick = { onPlusClick() }) {
                        Icon(
                            imageVector = Icons.Default.PlusOne,
                            contentDescription = stringResource(NekoRes.string.grid_item_plus_one)
                        )
                    }
                    LinearProgressIndicator(
                        progress = { progress },
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
