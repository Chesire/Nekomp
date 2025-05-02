package com.chesire.nekomp.feature.discover.ui.pane

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.chesire.nekomp.core.model.Type
import com.chesire.nekomp.feature.discover.ui.DiscoverItem
import com.chesire.nekomp.feature.discover.ui.TrendingState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun TrendingPane(
    trendingState: TrendingState,
    onItemClick: (DiscoverItem) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        TrendingSection(
            title = "Trending anime",
            items = trendingState.trendingAnime,
            onItemClick = onItemClick
        )
        TrendingSection(
            title = "Trending manga",
            items = trendingState.trendingManga,
            onItemClick = onItemClick
        )
        TrendingSection(
            title = "Top rated anime",
            items = trendingState.topRatedAnime,
            onItemClick = onItemClick
        )
        TrendingSection(
            title = "Top rated manga",
            items = trendingState.topRatedManga,
            onItemClick = onItemClick
        )
        TrendingSection(
            title = "Most popular anime",
            items = trendingState.mostPopularAnime,
            onItemClick = onItemClick
        )
        TrendingSection(
            title = "Most popular manga",
            items = trendingState.mostPopularManga,
            onItemClick = onItemClick
        )
    }
}

@Composable
private fun TrendingSection(
    title: String,
    items: ImmutableList<DiscoverItem>,
    onItemClick: (DiscoverItem) -> Unit
) {
    Column {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .safeContentPadding(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = items,
                key = { it.kitsuId }
            ) {
                TrendingDisplay(
                    discoverItem = it,
                    modifier = Modifier.fillMaxHeight(),
                    onItemClick = onItemClick
                )
            }
        }
    }
}

@Composable
private fun TrendingDisplay(
    discoverItem: DiscoverItem,
    modifier: Modifier = Modifier,
    onItemClick: (DiscoverItem) -> Unit
) {
    Card(
        onClick = { onItemClick(discoverItem) },
        modifier = modifier.width(256.dp).fillMaxHeight()
    ) {
        Box {
            AsyncImage(
                model = discoverItem.coverImage,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.3f
            )
            Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(discoverItem.title, modifier = Modifier.weight(1f))
                    if (discoverItem.isTracked) {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = null,
                            modifier = Modifier.alpha(0.7f)
                        )
                    }
                }
                // current rating
                // Synopsis
            }
        }
    }
}

@Composable
@Preview
private fun Preview() {
    val model = DiscoverItem(
        kitsuId = 1,
        title = "Item",
        type = Type.Anime,
        subType = "OVA",
        status = "current",
        synopsis = "",
        averageRating = "81.13",
        totalLength = 12,
        coverImage = "",
        posterImage = "",
        isTracked = false,
        isPendingTrack = false,
    )
    val state = TrendingState(
        trendingAnime = persistentListOf(model),
        trendingManga = persistentListOf(model),
        topRatedAnime = persistentListOf(model),
        topRatedManga = persistentListOf(model),
        mostPopularAnime = persistentListOf(model),
        mostPopularManga = persistentListOf(model)
    )
    TrendingPane(
        trendingState = state,
        onItemClick = {}
    )
}
