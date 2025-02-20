@file:OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalMaterial3AdaptiveApi::class,
    ExperimentalSharedTransitionApi::class
)

package com.chesire.nekomp.feature.discover.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlinx.collections.immutable.ImmutableList
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DiscoverScreen(viewModel: DiscoverViewModel = koinViewModel()) {
    val state by viewModel.uiState.collectAsState()

    Render(
        state = state,
        execute = { viewModel.execute(it) }
    )
}

// List/Detail/Support(?)
// List shows a search bar at the top, with the trending series below it
// Clicking the search bar shows a drop down of search options
// Executing search goes to the detail screen
// Detail screen shows the list of searched series
// Support screen shows the details about a series (?) depending on implementation

// Default list screen should show the current trending series
// Search bar at top of list, clicking it should change to a search screen
// Add a top rated section?
// Clicking on a trending item shows the details for it?

@Composable
private fun Render(
    state: UIState,
    execute: (ViewAction) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val navigator = rememberListDetailPaneScaffoldNavigator<DiscoverItem>()
    val isListAndDetailVisible =
        navigator.scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded &&
            navigator.scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Expanded

    LaunchedEffect(state.viewEvent) {
        when (state.viewEvent) {
            null -> Unit
        }

        execute(ViewAction.ObservedViewEvent)
    }

    // TODO: Switch to predictive
    BackHandler(enabled = navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        SharedTransitionLayout(modifier = Modifier.padding(paddingValues)) {
            AnimatedContent(
                targetState = isListAndDetailVisible,
                label = "listDetailAnimatedContent"
            ) {
                ListDetailPaneScaffold(
                    directive = navigator.scaffoldDirective,
                    value = navigator.scaffoldValue,
                    listPane = {
                        AnimatedPane {
                            ListContent(
                                trendingAnime = state.trendingAnime,
                                trendingManga = state.trendingManga,
                                topRatedAnime = state.topRatedAnime,
                                topRatedManga = state.topRatedManga,
                                mostPopularAnime = state.mostPopularAnime,
                                mostPopularManga = state.mostPopularManga,
                                onItemClick = { item ->
                                    navigator.navigateTo(
                                        ListDetailPaneScaffoldRole.Detail,
                                        item
                                    )
                                },
                                onTrackClick = { item ->
                                    execute(ViewAction.TrackTrendingItemClick(item))
                                }
                            )
                        }
                    },
                    detailPane = {
                        AnimatedPane {
                            DetailContent(
                                item = navigator.currentDestination?.content,
                                showBack = navigator.scaffoldValue.primary == PaneAdaptedValue.Expanded,
                                goBack = { navigator.navigateBack() }
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun ListContent(
    trendingAnime: ImmutableList<DiscoverItem>,
    trendingManga: ImmutableList<DiscoverItem>,
    topRatedAnime: ImmutableList<DiscoverItem>,
    topRatedManga: ImmutableList<DiscoverItem>,
    mostPopularAnime: ImmutableList<DiscoverItem>,
    mostPopularManga: ImmutableList<DiscoverItem>,
    onItemClick: (DiscoverItem) -> Unit,
    onTrackClick: (DiscoverItem) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            label = {
                Text("Search")
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            }
        )
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

@Composable
private fun DetailContent(
    item: DiscoverItem?,
    showBack: Boolean,
    goBack: () -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (item != null) {
            if (showBack) {
                IconButton(
                    onClick = goBack,
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Icon(
                        painter = rememberVectorPainter(Icons.AutoMirrored.Filled.ArrowBack),
                        contentDescription = "Go back"
                    )
                }
            }
        } else {
            Text("No entry selected")
        }
    }
}
