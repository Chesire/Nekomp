@file:OptIn(ExperimentalMaterial3Api::class)

package com.chesire.nekomp.feature.profile.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.chesire.nekomp.core.resources.NekoRes
import nekomp.core.resources.generated.resources.nav_content_description_settings
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel(),
    navigateToSettings: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Render(
        state = state,
        navigateToSettings = navigateToSettings,
        execute = { viewModel.execute(it) }
    )
}

@Composable
private fun Render(
    state: UIState,
    navigateToSettings: () -> Unit,
    execute: (ViewAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Profile")
                },
                actions = {
                    IconButton(onClick = navigateToSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(NekoRes.string.nav_content_description_settings)
                        )
                    }
                }
            )
        }
    ) {
        Text("Profile")
    }
}
