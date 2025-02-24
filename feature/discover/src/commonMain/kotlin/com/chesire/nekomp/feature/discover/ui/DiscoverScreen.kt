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
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
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
            is ViewEvent.ShowFailure -> snackbarHostState.showSnackbar(state.viewEvent.errorString)
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
                        var displayedPaneType by remember { mutableStateOf(ListPaneType.Trending) }
                        AnimatedPane {
                            ListPane(
                                listPaneType = displayedPaneType,
                                searchText = state.searchTerm,
                                recentSearches = state.recentSearches,
                                trendingState = state.trendingState,
                                resultsState = state.resultsState,
                                execute = execute,
                                onItemClick = { item ->
                                    execute(ViewAction.ShowDetail(item))
                                    navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
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
                                trackItem = { execute(ViewAction.TrackItemClick(it)) },
                                goBack = { navigator.navigateBack() }
                            )
                        }
                    }
                )
            }
        }
    }
}
