package com.chesire.nekomp.feature.discover.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun TrendingPane(
    trendingAnime: ImmutableList<DiscoverItem>,
    trendingManga: ImmutableList<DiscoverItem>,
    topRatedAnime: ImmutableList<DiscoverItem>,
    topRatedManga: ImmutableList<DiscoverItem>,
    mostPopularAnime: ImmutableList<DiscoverItem>,
    mostPopularManga: ImmutableList<DiscoverItem>,
    onItemClick: (DiscoverItem) -> Unit,
    onTrackClick: (DiscoverItem) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        TrendingSection(
            title = "Trending anime",
            items = trendingAnime,
            onItemClick = onItemClick,
            onTrackClick = onTrackClick
        )
        TrendingSection(
            title = "Trending manga",
            items = trendingManga,
            onItemClick = onItemClick,
            onTrackClick = onTrackClick
        )
        TrendingSection(
            title = "Top rated anime",
            items = topRatedAnime,
            onItemClick = onItemClick,
            onTrackClick = onTrackClick
        )
        TrendingSection(
            title = "Top rated manga",
            items = topRatedManga,
            onItemClick = onItemClick,
            onTrackClick = onTrackClick
        )
        TrendingSection(
            title = "Most popular anime",
            items = mostPopularAnime,
            onItemClick = onItemClick,
            onTrackClick = onTrackClick
        )
        TrendingSection(
            title = "Most popular manga",
            items = mostPopularManga,
            onItemClick = onItemClick,
            onTrackClick = onTrackClick
        )
    }
}

@Composable
private fun TrendingSection(
    title: String,
    items: ImmutableList<DiscoverItem>,
    onItemClick: (DiscoverItem) -> Unit,
    onTrackClick: (DiscoverItem) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .horizontalScroll(rememberScrollState())
                .safeContentPadding(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items.forEach {
                TrendingDisplay(
                    discoverItem = it,
                    modifier = Modifier.fillMaxHeight(),
                    onItemClick = onItemClick,
                    onTrackClick = onTrackClick
                )
            }
        }
    }
}

@Composable
private fun TrendingDisplay(
    discoverItem: DiscoverItem,
    modifier: Modifier = Modifier,
    onItemClick: (DiscoverItem) -> Unit,
    onTrackClick: (DiscoverItem) -> Unit
) {
    Card(
        onClick = { onItemClick(discoverItem) },
        modifier = modifier.width(256.dp)
    ) {
        Box {
            AsyncImage(
                model = discoverItem.coverImage,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillHeight,
                alpha = 0.3f
            )
            Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                // image
                Text(discoverItem.title) // title
                // current rating
                // Synopsis
                // track button
                Spacer(Modifier.weight(1f))
                if (!discoverItem.isTracked) {
                    ElevatedButton(
                        onClick = { onTrackClick(discoverItem) },
                        modifier = Modifier.align(Alignment.End),
                        enabled = !discoverItem.isPendingTrack
                    ) {
                        if (discoverItem.isPendingTrack) {
                            CircularProgressIndicator()
                        } else {
                            Text("Track")
                        }
                    }
                }
            }
        }
    }
}
