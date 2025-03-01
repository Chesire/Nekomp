@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)

package com.chesire.nekomp.feature.profile.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.chesire.nekomp.core.resources.NekoRes
import kotlin.math.absoluteValue
import nekomp.core.resources.generated.resources.nav_content_description_settings
import org.jetbrains.compose.resources.stringResource
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
        navigateToSettings = navigateToSettings,
        execute = { viewModel.execute(it) }
    )
}

@Composable
private fun Render(
    state: UIState,
    goBack: () -> Unit,
    navigateToSettings: () -> Unit,
    execute: (ViewAction) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState()
    )
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Box {
                AsyncImage(
                    model = state.coverImage,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.TopCenter)
                        .heightIn(max = TopAppBarDefaults.LargeAppBarExpandedHeight)
                        .fillMaxHeight((scrollBehavior.state.collapsedFraction - 1f).absoluteValue),
                    contentScale = ContentScale.Crop,
                    alpha = (scrollBehavior.state.collapsedFraction - 1f).absoluteValue
                )
                LargeTopAppBar(
                    title = {
                        Text(
                            text = "Profile",
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = goBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null // Go back string
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = navigateToSettings) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = stringResource(NekoRes.string.nav_content_description_settings)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Transparent
                    ),
                    scrollBehavior = scrollBehavior
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Dummy data1")
            Text("Dummy data23")
            Text("Dummy data3")
            Text("Dummy data4")
            Text("Dummy data5")
            Text("Dummy data6")
            Text("Dummy data7")
            Text("Dummy data8")
            Text("Dummy data9")
            Text("Dummy data0")
            Text("Dummy data1")
            Text("Dummy data2")
            Text("Dummy data3")
            Text("Dummy data4")
            Text("Dummy data5")
            Text("Dummy data6")
            Text("Dummy data7")
            Text("Dummy data8")
            Text("Dummy data9")
            Text("Dummy data0")
            Text("Dummy data12")
            Text("Dummy data2")
            Text("Dummy data3")
            Text("Dummy data4")
            Text("Dummy data5")
            Text("Dummy data6")
            Text("Dummy data7")
            Text("Dummy data8")
            Text("Dummy data9")
            Text("Dummy data0")
            Text("Dummy data1")
            Text("Dummy data2")
            Text("Dummy data3")
            Text("Dummy data4")
            Text("Dummy data5")
            Text("Dummy data6")
            Text("Dummy data7")
            Text("Dummy data8")
            Text("Dummy data9")
            Text("Dummy data1")
            Text("Dummy data23")
            Text("Dummy data5")
            Text("Dummy data5")
            Text("Dummy data6")
            Text("Dummy data7")
            Text("Dummy data8")
            Text("Dummy data9")
            Text("Dummy data09")
            Text("Dummy data1")
            Text("Dummy data2")
            Text("Dummy data3")
            Text("Dummy data4")
            Text("Dummy data5")
            Text("Dummy data6")
            Text("Dummy data7")
            Text("Dummy data8")
            Text("Dummy data9")
            Text("Dummy data1")
            Text("Dummy data2")
            Text("Dummy data3")
            Text("Dummy data4")
            Text("Dummy data5")
            Text("Dummy data6")
            Text("Dummy data7")
            Text("Dummy data8")
            Text("Dummy data9")
            Text("Dummy data1")
            Text("Dummy data2")
            Text("Dummy data3")
            Text("Dummy data4")
            Text("Dummy data5")
            Text("Dummy data6")
            Text("Dummy data7")
            Text("Dummy data8")
            Text("Dummy data9")
            Text("Dummy data1")
        }
    }
}
