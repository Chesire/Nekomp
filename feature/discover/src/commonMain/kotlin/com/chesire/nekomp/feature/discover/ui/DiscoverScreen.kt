@file:OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalMaterial3AdaptiveApi::class,
    ExperimentalSharedTransitionApi::class
)

package com.chesire.nekomp.feature.discover.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
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
                            DetailPane(
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

private enum class ListPaneType {
    Trending,
    Search,
    Results
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
    val focus = LocalFocusManager.current
    var displayedPaneType by remember { mutableStateOf(ListPaneType.Trending) }
    BackHandler(enabled = displayedPaneType != ListPaneType.Trending) {
        when (displayedPaneType) {
            ListPaneType.Trending -> Unit
            ListPaneType.Search -> {
                displayedPaneType = ListPaneType.Trending
                focus.clearFocus(true)
            }

            ListPaneType.Results -> {
                displayedPaneType = ListPaneType.Search
            }
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row {
            AnimatedVisibility(
                visible = displayedPaneType != ListPaneType.Trending,
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                IconButton(
                    onClick = {
                        displayedPaneType = ListPaneType.Trending
                        focus.clearFocus(true)
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            }
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .animateContentSize()
                    .onFocusEvent {
                        if (it.hasFocus) {
                            displayedPaneType = ListPaneType.Search
                        }
                    },
                label = {
                    Text("Search")
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                }
            )
        }
        when (displayedPaneType) {
            ListPaneType.Trending -> TrendingPane(
                trendingAnime = trendingAnime,
                trendingManga = trendingManga,
                topRatedAnime = topRatedAnime,
                topRatedManga = topRatedManga,
                mostPopularAnime = mostPopularAnime,
                mostPopularManga = mostPopularManga,
                onItemClick = onItemClick,
                onTrackClick = onTrackClick
            )

            ListPaneType.Search -> SearchPane()
            ListPaneType.Results -> ResultsPane()
        }
    }
}
