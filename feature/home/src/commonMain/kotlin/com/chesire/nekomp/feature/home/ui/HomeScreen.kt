@file:OptIn(ExperimentalMaterial3Api::class)

package com.chesire.nekomp.feature.home.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonOutline
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
import nekomp.core.resources.generated.resources.nav_content_description_profile
import org.jetbrains.compose.resources.stringResource
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
                    Text("Welcome back, ${state.username}")
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
    ) {
        Text("Home")
    }
}
