@file:OptIn(ExperimentalMaterial3Api::class)

package com.chesire.nekomp.feature.library.ui.pane

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.chesire.nekomp.core.resources.NekoRes
import com.chesire.nekomp.core.ui.NekompTheme
import com.chesire.nekomp.feature.library.ui.Entry
import kotlin.math.absoluteValue
import nekomp.core.resources.generated.resources.discover_detail_no_entry
import nekomp.core.resources.generated.resources.nav_content_description_go_back
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DetailPane(
    entry: Entry?,
    showBack: Boolean,
    goBack: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState()
    )
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Box {
                AsyncImage(
                    model = entry?.coverImage,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.TopCenter)
                        .heightIn(max = TopAppBarDefaults.LargeAppBarExpandedHeight)
                        .fillMaxHeight((scrollBehavior.state.collapsedFraction - 1f).absoluteValue),
                    contentScale = ContentScale.Crop,
                    alpha = (scrollBehavior.state.collapsedFraction - 1f).absoluteValue
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                .5F to Color.Transparent,
                                .7f to MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                                .8f to MaterialTheme.colorScheme.background.copy(alpha = 0.9f),
                                1F to MaterialTheme.colorScheme.background.copy(alpha = 1f)
                            )
                        )
                )
            }
            LargeTopAppBar(
                title = {
                    Text(
                        text = entry?.title
                            ?: stringResource(NekoRes.string.discover_detail_no_entry),
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = scrollBehavior.state.collapsedFraction
                        )
                    )
                },
                navigationIcon = {
                    if (entry != null && showBack) {
                        IconButton(onClick = goBack) {
                            Icon(
                                painter = rememberVectorPainter(Icons.AutoMirrored.Filled.ArrowBack),
                                contentDescription = stringResource(
                                    NekoRes.string.nav_content_description_go_back
                                )
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                ),
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        if (entry != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

            }
        }
    }
}

@Composable
@Preview
private fun Preview() {
    NekompTheme {
        DetailPane(
            entry = Entry(
                entryId = 1,
                title = "Title",
                posterImage = "",
                coverImage = "",
                progressPercent = 0.5f,
                progress = 5,
                progressDisplay = "5/10",
                isUpdating = false,
                canUpdate = true
            ),
            showBack = true,
            goBack = {}
        )
    }
}
