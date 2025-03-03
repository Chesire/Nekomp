@file:OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalMaterial3AdaptiveApi::class,
    ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class
)

package com.chesire.nekomp.feature.library.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.chesire.nekomp.core.ui.component.SettingSheet
import com.chesire.nekomp.feature.library.ui.pane.DetailPane
import com.chesire.nekomp.feature.library.ui.pane.ListPane
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LibraryScreen(viewModel: LibraryViewModel = koinViewModel()) {
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
    val snackbarHostState = remember { SnackbarHostState() }
    val navigator = rememberListDetailPaneScaffoldNavigator<Entry>()
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
                        AnimatedPane {
                            ListPane(
                                entries = state.entries,
                                currentViewType = state.viewType,
                                execute = execute,
                                onEntryClick = { entry ->
                                    scope.launch {
                                        navigator.navigateTo(
                                            ListDetailPaneScaffoldRole.Detail,
                                            entry
                                        )
                                    }
                                }
                            )
                        }
                    },
                    detailPane = {
                        AnimatedPane {
                            DetailPane(
                                entry = navigator.currentDestination?.contentKey,
                                showBack = navigator.scaffoldValue.primary == PaneAdaptedValue.Expanded,
                                goBack = {
                                    scope.launch {
                                        navigator.navigateBack()
                                    }
                                }
                            )
                        }
                    }
                )
            }
        }

        BottomSheetEventHandler(
            bottomSheet = state.bottomSheet,
            execute = execute
        )
    }
}

@Composable
private fun BottomSheetEventHandler(
    bottomSheet: LibraryBottomSheet?,
    execute: (ViewAction) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    var rememberedSheet by remember { mutableStateOf(bottomSheet) }
    if (bottomSheet != null) {
        rememberedSheet = bottomSheet
    }

    LaunchedEffect(bottomSheet) {
        if (bottomSheet == null) {
            coroutineScope.launch {
                sheetState.hide()
            }.invokeOnCompletion {
                rememberedSheet = null
            }
        }
    }

    when (val sheet = rememberedSheet) {
        is LibraryBottomSheet.ViewTypeBottomSheet -> SettingSheet(
            sheetState = sheetState,
            title = "View type",
            entries = sheet.types,
            selectedEntry = sheet.selectedType,
            execute = { execute(ViewAction.ViewTypeChosen(it)) }
        )

        null -> Unit
    }
}

@Composable
@Preview
private fun Preview() {
    val state = UIState(
        entries = persistentListOf<Entry>(
            Entry(0, "Title1", "", "", 0f, 0)
        )
    )
    Render(
        state = state,
        execute = {}
    )
}
