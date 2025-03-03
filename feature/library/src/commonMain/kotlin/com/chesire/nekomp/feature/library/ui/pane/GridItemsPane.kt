package com.chesire.nekomp.feature.library.ui.pane

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.chesire.nekomp.feature.library.ui.Entry
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun GridItemsPane(
    entries: ImmutableList<Entry>,
    modifier: Modifier = Modifier,
    onEntryClick: (Entry) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(128.dp),
        modifier = modifier
            .padding(horizontal = 16.dp)
            .animateContentSize(),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = entries,
            key = { it.id }
        ) {
            GridItem(
                entry = it,
                modifier = Modifier,
                onEntryClick = onEntryClick
            )
        }
    }
}

@Composable
private fun GridItem(
    entry: Entry,
    modifier: Modifier = Modifier,
    onEntryClick: (Entry) -> Unit
) {
    Card(
        onClick = { onEntryClick(entry) },
        modifier = modifier.wrapContentWidth()
    ) {
        Box(modifier = Modifier.wrapContentSize()) {
            AsyncImage(
                model = entry.image,
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                alpha = 0.3f
            )
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .width(IntrinsicSize.Min)
                    .matchParentSize()
                    .align(Alignment.BottomStart),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = entry.title,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
