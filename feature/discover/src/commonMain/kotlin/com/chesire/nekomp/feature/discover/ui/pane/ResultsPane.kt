package com.chesire.nekomp.feature.discover.ui.pane

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.chesire.nekomp.core.model.Type
import com.chesire.nekomp.feature.discover.ui.DiscoverItem
import com.chesire.nekomp.feature.discover.ui.ResultsState
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.ui.tooling.preview.Preview

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
            SearchDisplay(
                item = item,
                modifier = Modifier.animateItem(),
                onItemClick = onItemClick
            )
        }
    }
}

@Composable
private fun SearchDisplay(
    item: DiscoverItem,
    modifier: Modifier = Modifier,
    onItemClick: (DiscoverItem) -> Unit
) {
    Card(
        onClick = { onItemClick(item) },
        modifier = modifier.wrapContentWidth()
    ) {
        Box(modifier = Modifier.wrapContentSize()) {
            AsyncImage(
                model = item.posterImage,
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                alpha = 0.3f
            )
            if (item.isTracked) {
                Icon(
                    imageVector = Icons.Default.Bookmark,
                    contentDescription = null,
                    modifier = Modifier
                        .alpha(0.7f)
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                )
            }
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

@Composable
@Preview
private fun Preview() {
    val state = ResultsState(
        searchResults = persistentListOf(
            DiscoverItem(
                id = 1,
                title = "Item",
                type = Type.Anime,
                subType = "OVA",
                synopsis = "",
                coverImage = "",
                posterImage = "",
                isTracked = false,
                isPendingTrack = false,
            )
        )
    )
    ResultsPane(
        resultsState = state,
        onItemClick = {}
    )
}
