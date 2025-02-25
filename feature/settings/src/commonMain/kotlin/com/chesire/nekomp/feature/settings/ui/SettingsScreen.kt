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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch
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
                subtitle = state.currentTheme,
                startComposable = {
                    Icon(imageVector = Icons.Default.FormatPaint, contentDescription = null)
                },
                onClick = { execute(ViewAction.ThemeClick) }
            )
            Setting(
                title = "Title Language",
                subtitle = state.titleLanguage,
                startComposable = {
                    Icon(imageVector = Icons.Default.Language, contentDescription = null)
                },
                onClick = { execute(ViewAction.TitleLanguageClick) }
            )
            Setting(
                title = "Image Quality",
                subtitle = state.imageQuality,
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

        BottomSheetEventHandler(
            bottomSheet = state.bottomSheet,
            execute = execute
        )
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

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
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
private fun BottomSheetEventHandler(
    bottomSheet: SettingsBottomSheet?,
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
        is SettingsBottomSheet.ThemeBottomSheet -> SettingsSheet(
            sheetState = sheetState,
            title = "Theme",
            entries = sheet.themes,
            selectedEntry = sheet.selectedTheme,
            execute = { execute(ViewAction.ThemeChosen(it)) }
        )

        null -> Unit
    }
}

@Composable
private fun <T : Enum<T>> SettingsSheet(
    sheetState: SheetState,
    title: String,
    entries: ImmutableList<T>,
    selectedEntry: T,
    execute: (T?) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { execute(null) },
        content = {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                entries.forEach { entry ->
                    Row(
                        modifier = Modifier
                            .clickable(
                                enabled = true,
                                onClick = { execute(entry) }
                            )
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = entry.name,
                            modifier = Modifier.weight(1f)
                        )
                        RadioButton(
                            selected = entry == selectedEntry,
                            onClick = null
                        )
                    }
                }
            }
        },
        sheetState = sheetState
    )
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
