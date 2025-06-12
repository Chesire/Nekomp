@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)

package com.chesire.nekomp.feature.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.DeveloperMode
import androidx.compose.material.icons.outlined.FormatPaint
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chesire.nekomp.core.resources.NekoRes
import com.chesire.nekomp.core.ui.component.SettingSheet
import com.chesire.nekomp.core.ui.util.ifTrue
import kotlinx.coroutines.launch
import nekomp.core.resources.generated.resources.nav_content_description_go_back
import nekomp.core.resources.generated.resources.nav_settings
import nekomp.core.resources.generated.resources.settings_help_body
import nekomp.core.resources.generated.resources.settings_help_title
import nekomp.core.resources.generated.resources.settings_image_quality_title
import nekomp.core.resources.generated.resources.settings_logout_body
import nekomp.core.resources.generated.resources.settings_logout_title
import nekomp.core.resources.generated.resources.settings_rate_series_body
import nekomp.core.resources.generated.resources.settings_rate_series_title
import nekomp.core.resources.generated.resources.settings_section_account
import nekomp.core.resources.generated.resources.settings_section_application_preferences
import nekomp.core.resources.generated.resources.settings_section_support
import nekomp.core.resources.generated.resources.settings_theme_title
import nekomp.core.resources.generated.resources.settings_title_language_title
import nekomp.core.resources.generated.resources.settings_version_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel(),
    goBack: () -> Unit,
    onLoggedOut: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Render(
        state = state,
        goBack = goBack,
        onLoggedOut = onLoggedOut,
        execute = { viewModel.execute(it) }
    )
}

@Suppress("LongMethod")
@Composable
private fun Render(
    state: UIState,
    goBack: () -> Unit,
    onLoggedOut: () -> Unit,
    execute: (ViewAction) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val uriHandler = LocalUriHandler.current
    LaunchedEffect(state.viewEvent) {
        when (state.viewEvent) {
            ViewEvent.LoggedOut -> onLoggedOut()
            null -> Unit
        }

        if (state.viewEvent != null) {
            execute(ViewAction.ObservedViewEvent)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(NekoRes.string.nav_settings)) },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(
                                NekoRes.string.nav_content_description_go_back
                            )
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Heading(stringResource(NekoRes.string.settings_section_application_preferences))
            Setting(
                title = stringResource(NekoRes.string.settings_theme_title),
                subtitle = state.currentTheme,
                startComposable = {
                    Icon(imageVector = Icons.Outlined.FormatPaint, contentDescription = null)
                },
                onClick = { execute(ViewAction.ThemeClick) }
            )
            Setting(
                title = stringResource(NekoRes.string.settings_title_language_title),
                subtitle = state.titleLanguage,
                startComposable = {
                    Icon(imageVector = Icons.Outlined.Language, contentDescription = null)
                },
                onClick = { execute(ViewAction.TitleLanguageClick) }
            )
            Setting(
                title = stringResource(NekoRes.string.settings_image_quality_title),
                subtitle = state.imageQuality,
                startComposable = {
                    Icon(imageVector = Icons.Outlined.Image, contentDescription = null)
                },
                onClick = { execute(ViewAction.ImageQualityClick) }
            )
            Setting(
                title = stringResource(NekoRes.string.settings_rate_series_title),
                subtitle = stringResource(NekoRes.string.settings_rate_series_body),
                startComposable = {
                    Icon(imageVector = Icons.Outlined.RateReview, contentDescription = null)
                },
                endComposable = {
                    Checkbox(checked = state.rateChecked, onCheckedChange = null)
                },
                onClick = { execute(ViewAction.RateChanged) }
            )

            Heading(stringResource(NekoRes.string.settings_section_account))
            // TODO: Add confirmation dialog
            Setting(
                title = stringResource(NekoRes.string.settings_logout_title),
                subtitle = stringResource(NekoRes.string.settings_logout_body),
                startComposable = {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Logout, contentDescription = null)
                },
                onClick = { execute(ViewAction.LogoutClick) }
            )

            Heading(stringResource(NekoRes.string.settings_section_support))
            Setting(
                title = stringResource(NekoRes.string.settings_help_title),
                subtitle = stringResource(NekoRes.string.settings_help_body),
                startComposable = {
                    Icon(imageVector = Icons.Outlined.DeveloperMode, contentDescription = null)
                },
                onClick = { uriHandler.openUri(state.helpUrl) }
            )
            Setting(
                title = stringResource(NekoRes.string.settings_version_title),
                subtitle = state.version,
                startComposable = {
                    Icon(imageVector = Icons.Outlined.Build, contentDescription = null)
                },
                onClick = null
            )
        }

        BottomSheetEventHandler(
            bottomSheet = state.bottomSheet,
            execute = execute
        )
    }
}

@Composable
private fun Heading(title: String) {
    Text(
        text = title,
        modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun Setting(
    title: String,
    subtitle: String,
    startComposable: (@Composable () -> Unit)? = null,
    endComposable: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)?
) {
    Row(
        modifier = Modifier
            .ifTrue(onClick != null) {
                clickable(enabled = onClick != null, onClick = { onClick?.invoke() })
            }
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (startComposable != null) {
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
            ) {
                startComposable()
            }
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
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
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
        is SettingsBottomSheet.ThemeBottomSheet -> SettingSheet(
            sheetState = sheetState,
            title = stringResource(NekoRes.string.settings_theme_title),
            entries = sheet.themes,
            selectedEntry = sheet.selectedTheme,
            execute = { execute(ViewAction.ThemeChosen(it)) }
        )

        is SettingsBottomSheet.TitleLanguageBottomSheet -> SettingSheet(
            sheetState = sheetState,
            title = stringResource(NekoRes.string.settings_title_language_title),
            entries = sheet.languages,
            selectedEntry = sheet.selectedLanguage,
            execute = { execute(ViewAction.TitleLanguageChosen(it)) }
        )

        is SettingsBottomSheet.ImageQualityBottomSheet -> SettingSheet(
            sheetState = sheetState,
            title = stringResource(NekoRes.string.settings_image_quality_title),
            entries = sheet.qualities,
            selectedEntry = sheet.selectedQuality,
            execute = { execute(ViewAction.ImageQualityChosen(it)) }
        )

        null -> Unit
    }
}

@Composable
@Preview
private fun Preview() {
    val state = UIState(
        currentTheme = "Dark",
        titleLanguage = "Canonical",
        imageQuality = "Highest",
        rateChecked = true,
        version = "2.1.2"
    )
    Render(
        state = state,
        goBack = {},
        onLoggedOut = {},
        execute = {}
    )
}
