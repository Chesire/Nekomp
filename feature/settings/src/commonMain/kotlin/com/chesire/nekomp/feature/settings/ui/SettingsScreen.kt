@file:OptIn(ExperimentalMaterial3Api::class)

package com.chesire.nekomp.feature.settings.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = koinViewModel()) {
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

    LaunchedEffect(state.viewEvent) {
        when (state.viewEvent) {
            null -> Unit
        }

        execute(ViewAction.ObservedViewEvent)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Setting(
                title = "Theme",
                subtitle = "Which theme the application uses",
                startComposable = {
                    Icon(imageVector = Icons.Default.FormatPaint, contentDescription = null)
                },
                onClick = { execute(ViewAction.ThemeClick) }
            )
            Setting(
                title = "Title Language",
                subtitle = "Language to use for displaying Anime & Manga titles",
                startComposable = {
                    Icon(imageVector = Icons.Default.Language, contentDescription = null)
                },
                onClick = { execute(ViewAction.TitleLanguageClick) }
            )
            Setting(
                title = "Image Quality",
                subtitle = "Quality of the displayed images within the application",
                startComposable = {
                    Icon(imageVector = Icons.Default.Image, contentDescription = null)
                },
                onClick = { execute(ViewAction.ImageQualityClick) }
            )
            Setting(
                title = "Rate Series",
                subtitle = "Prompt to rate series when finishing it",
                startComposable = {
                    Icon(imageVector = Icons.Default.RateReview, contentDescription = null)
                },
                endComposable = {
                    Checkbox(checked = state.rateCheckbox, onCheckedChange = null)
                },
                onClick = { execute(ViewAction.RateChanged) }
            )
            Setting(
                title = "Logout",
                subtitle = "Logout of the application",
                startComposable = {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Logout, contentDescription = null)
                },
                onClick = { execute(ViewAction.LogoutClick) }
            )
            Setting(
                title = "Version",
                subtitle = state.version,
                onClick = { }
            )
        }
    }
}

@Composable
private fun Setting(
    title: String,
    subtitle: String,
    startComposable: (@Composable () -> Unit)? = null,
    endComposable: (@Composable () -> Unit)? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable(enabled = true, onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (startComposable != null) {
            startComposable()
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium
            )
            if (subtitle.isNotBlank()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        if (endComposable != null) {
            endComposable()
        }
    }
}

@Composable
@Preview
private fun Preview() {
    val state = UIState()
    Render(
        state = state,
        execute = {}
    )
}
