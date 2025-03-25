@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package com.chesire.nekomp.feature.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.chesire.nekomp.core.resources.NekoRes
import com.chesire.nekomp.feature.home.ui.components.AiringListComponent
import com.chesire.nekomp.feature.home.ui.components.TrendingListComponent
import com.chesire.nekomp.feature.home.ui.components.WatchListComponent
import kotlinx.collections.immutable.persistentListOf
import nekomp.core.resources.generated.resources.nav_content_description_profile
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    navigateToProfile: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Render(
        state = state,
        navigateToProfile = navigateToProfile,
        execute = { viewModel.execute(it) }
    )
}

@Composable
private fun Render(
    state: UIState,
    navigateToProfile: () -> Unit,
    execute: (ViewAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Welcome back, ${state.username}",
                        minLines = 1,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(onClick = navigateToProfile) {
                        Icon(
                            imageVector = Icons.Default.PersonOutline,
                            contentDescription = stringResource(NekoRes.string.nav_content_description_profile)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        // TODO: Add sealed class for UI states so we can hide the UI until its loaded
        // Maybe this should be a SupportingPane layout? Look into it when we have the trending and library detail
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            WatchListComponent(
                watchItems = state.watchList,
                onWatchItemClick = { execute(ViewAction.WatchItemClick(it)) },
                onPlusOneClick = { execute(ViewAction.WatchItemPlusOneClick(it)) }
            )
            if (state.airing.isNotEmpty()) {
                AiringListComponent(
                    airingItems = state.airing,
                    onAiringItemClick = { execute(ViewAction.AiringItemClick(it)) }
                )
            }
            if (state.trendingAll.isNotEmpty()) {
                TrendingListComponent(
                    trendingAll = state.trendingAll,
                    trendingAnime = state.trendingAnime,
                    trendingManga = state.trendingManga,
                    onTrendItemClick = { execute(ViewAction.TrendItemClick(it)) }
                )
            }
        }
    }
}

@Composable
@Preview
private fun Preview() {
    val state = UIState(
        username = "Nekomp",
        watchList = persistentListOf(),
        trendingAll = persistentListOf(),
        trendingAnime = persistentListOf(),
        trendingManga = persistentListOf()
    )
    Render(
        state = state,
        navigateToProfile = {},
        execute = {}
    )
}
