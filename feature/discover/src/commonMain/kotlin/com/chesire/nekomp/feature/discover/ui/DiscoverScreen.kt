@file:OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalMaterial3AdaptiveApi::class,
    ExperimentalSharedTransitionApi::class
)

package com.chesire.nekomp.feature.discover.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.rememberPaneExpansionState
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.platform.LocalUriHandler
import com.chesire.nekomp.feature.discover.ui.pane.DetailPane
import com.chesire.nekomp.feature.discover.ui.pane.ListPane
import com.chesire.nekomp.feature.discover.ui.pane.ListPaneType
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
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
    val scope = rememberCoroutineScope()
    val uriHandler = LocalUriHandler.current
    val snackbarHostState = remember { SnackbarHostState() }
    val navigator = rememberListDetailPaneScaffoldNavigator<DiscoverItem>()
    val paneState = rememberPaneExpansionState().apply {
        setFirstPaneProportion(0.65f)
    }
    val isListAndDetailVisible =
        navigator.scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded &&
            navigator.scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Expanded

    LaunchedEffect(state.viewEvent) {
        when (state.viewEvent) {
            is ViewEvent.OpenWebView -> uriHandler.openUri(state.viewEvent.url)
            is ViewEvent.ShowFailure -> snackbarHostState.showSnackbar(state.viewEvent.errorString)
            null -> Unit
        }

        execute(ViewAction.ObservedViewEvent)
    }

    // TODO: Switch to predictive
    BackHandler(enabled = navigator.canNavigateBack()) {
        scope.launch {
            navigator.navigateBack()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        SharedTransitionLayout {
            AnimatedContent(
                targetState = isListAndDetailVisible,
                label = "listDetailAnimatedContent"
            ) {
                ListDetailPaneScaffold(
                    directive = navigator.scaffoldDirective,
                    value = navigator.scaffoldValue,
                    listPane = {
                        var displayedPaneType by remember { mutableStateOf(ListPaneType.Trending) }
                        AnimatedPane {
                            ListPane(
                                listPaneType = displayedPaneType,
                                searchText = state.searchTerm,
                                recentSearches = state.recentSearches,
                                trendingState = state.trendingState,
                                resultsState = state.resultsState,
                                modifier = Modifier.padding(paddingValues),
                                execute = execute,
                                onItemClick = { item ->
                                    execute(ViewAction.ItemSelect(item))
                                    scope.launch {
                                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                                    }
                                },
                                updateListPaneType = {
                                    displayedPaneType = it
                                }
                            )
                        }
                    },
                    detailPane = {
                        AnimatedPane {
                            DetailPane(
                                detailState = state.detailState,
                                showBack = !isListAndDetailVisible &&
                                    navigator.scaffoldValue.primary == PaneAdaptedValue.Expanded,
                                execute = execute,
                                goBack = {
                                    scope.launch {
                                        navigator.navigateBack()
                                    }
                                }
                            )
                        }
                    },
                    paneExpansionState = paneState
                )
            }
        }
    }
}

@Composable
@Preview
private fun Preview() {
    val state = UIState(
        searchTerm = "",
        recentSearches = persistentListOf(),
        trendingState = TrendingState(),
        resultsState = ResultsState(),
        detailState = DetailState()
    )
    Render(
        state = state,
        execute = {}
    )
}
