@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)

package com.chesire.nekomp.feature.profile.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.chesire.nekomp.core.resources.NekoRes
import com.chesire.nekomp.feature.profile.ui.component.CompletedComponent
import com.chesire.nekomp.feature.profile.ui.component.FavoritesComponent
import com.chesire.nekomp.feature.profile.ui.component.HighlightsComponent
import com.chesire.nekomp.feature.profile.ui.component.UserComponent
import nekomp.core.resources.generated.resources.nav_content_description_go_back
import nekomp.core.resources.generated.resources.nav_content_description_settings
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel(),
    goBack: () -> Unit,
    navigateToSettings: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Render(
        state = state,
        goBack = goBack,
        navigateToSettings = navigateToSettings
    )
}

@Composable
private fun Render(
    state: UIState,
    goBack: () -> Unit,
    navigateToSettings: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(
                                NekoRes.string.nav_content_description_go_back
                            )
                        )
                    }
                },
                actions = {
                    IconButton(onClick = navigateToSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(
                                NekoRes.string.nav_content_description_settings
                            )
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            UserComponent(state.user)
            HighlightsComponent(state.highlights)
            CompletedComponent(state.backlog)
            FavoritesComponent(state.favorites)
        }
    }
}

@Composable
@Preview
private fun Preview() {
    val state = UIState(
        user = UserData(
            name = "Nekomp",
            about = "This is a large string to show for the about text"
        ),
        highlights = HighlightsData(
            episodesWatched = "32",
            chaptersRead = "300",
            timeSpentWatching = "3d 21h",
            seriesCompleted = "4"
        ),
        backlog = CompletedData(
            animeProgress = "1/10",
            animePercent = 0.10f,
            mangaProgress = "50/100",
            mangaPercent = 0.5f
        ),
        favorites = FavoritesData()
    )
    Render(
        state = state,
        goBack = {},
        navigateToSettings = {}
    )
}
