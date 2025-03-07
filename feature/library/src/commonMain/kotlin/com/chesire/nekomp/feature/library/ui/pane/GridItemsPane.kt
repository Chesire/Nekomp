package com.chesire.nekomp.feature.library.ui.pane

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.chesire.nekomp.feature.library.ui.Entry
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun GridItemsPane(
    entries: ImmutableList<Entry>,
    modifier: Modifier = Modifier,
    onEntryClick: (Entry) -> Unit,
    onPlusOneClick: (Entry) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(128.dp),
        modifier = modifier.padding(horizontal = 16.dp),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = entries,
            key = { it.entryId }
        ) {
            GridItem(
                entry = it,
                onEntryClick = onEntryClick,
                modifier = Modifier.animateItem(),
                onPlusOneClick = onPlusOneClick
            )
        }
    }
}

// TODO: Move this into CoreUI to share with home somehow
@Composable
private fun GridItem(
    entry: Entry,
    onEntryClick: (Entry) -> Unit,
    modifier: Modifier = Modifier,
    onPlusOneClick: (Entry) -> Unit
) {
    var width by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    Column(
        modifier = modifier.width(IntrinsicSize.Min),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            onClick = { onEntryClick(entry) },
            modifier = Modifier.wrapContentSize(),
        ) {
            Box(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .width(IntrinsicSize.Min)
            ) {
                AsyncImage(
                    model = entry.posterImage,
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
                                .7f to Color.Black.copy(alpha = 0.5f),
                                .8f to Color.Black.copy(alpha = 0.9f),
                                1F to Color.Black.copy(alpha = 1f)
                            )
                        )
                )
                Column(
                    modifier = Modifier.fillMaxHeight().width(width),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedIconButton(onClick = { onPlusOneClick(entry) }) {
                        Icon(
                            imageVector = Icons.Default.PlusOne,
                            contentDescription = null // TODO: Add content description
                        )
                    }
                    LinearProgressIndicator(
                        progress = { entry.progressPercent },
                        modifier = Modifier.height(4.dp),
                        gapSize = 0.dp,
                        drawStopIndicator = {}
                    )
                }
            }
        }
        Text(
            text = entry.title,
            style = MaterialTheme.typography.titleSmall,
            minLines = 2,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}
