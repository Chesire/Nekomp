package com.chesire.nekomp.feature.discover.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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

@Composable
internal fun ResultsPane(
    resultsState: ResultsState,
    onItemClick: (DiscoverItem) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(128.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            items = resultsState.searchResults,
            key = { it.id }
        ) { item ->
            Card(
                onClick = { onItemClick(item) },
                modifier = Modifier
                    .animateItem()
                    .wrapContentWidth()
            ) {
                Box(modifier = Modifier.wrapContentSize()) {
                    AsyncImage(
                        model = item.posterImage,
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
                            text = item.title,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
