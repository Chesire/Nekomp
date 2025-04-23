@file:OptIn(ExperimentalMaterial3Api::class)

package com.chesire.nekomp.feature.discover.ui.pane

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.chesire.nekomp.core.model.Type
import com.chesire.nekomp.core.resources.NekoRes
import com.chesire.nekomp.feature.discover.ui.DetailState
import com.chesire.nekomp.feature.discover.ui.DiscoverItem
import kotlin.math.absoluteValue
import nekomp.core.resources.generated.resources.nav_content_description_go_back
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun DetailPane(
    detailState: DetailState,
    showBack: Boolean,
    trackItem: (DiscoverItem) -> Unit,
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
                    model = detailState.currentItem?.coverImage,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.TopCenter)
                        .heightIn(max = TopAppBarDefaults.LargeAppBarExpandedHeight)
                        .fillMaxHeight((scrollBehavior.state.collapsedFraction - 1f).absoluteValue),
                    contentScale = ContentScale.Crop,
                    alpha = (scrollBehavior.state.collapsedFraction - 1f).absoluteValue
                )
            }
            LargeTopAppBar(
                title = {
                    Text(
                        text = detailState.currentItem?.title ?: "No entry selected",
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    if (detailState.currentItem != null && showBack) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (detailState.currentItem != null) {
                Row {
                    SuggestionChip(
                        onClick = {},
                        label = {
                            Text(text = detailState.currentItem.type.name)
                        },
                        modifier = Modifier
                            .clickable(
                                interactionSource = null,
                                indication = null,
                                enabled = false,
                                onClick = {}
                            ),
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = Color(0xFF66CC00)
                        ),
                        shape = RoundedCornerShape(32.dp)
                    )
                }
                // Do small chips for different parts
                // Chip for Anime/Manga
                // Chip for type (ova, series etc)
                // Synopsis below
                if (!detailState.currentItem.isTracked) {
                    ElevatedButton(
                        onClick = { trackItem(detailState.currentItem) },
                        enabled = !detailState.currentItem.isPendingTrack
                    ) {
                        if (detailState.currentItem.isPendingTrack) {
                            CircularProgressIndicator()
                        } else {
                            Text("Track")
                        }
                    }
                }
            } else {
                Text("No entry selected")
            }
        }
    }
}

@Composable
@Preview
private fun Preview() {
    val state = DetailState(
        currentItem = DiscoverItem(
            id = 1,
            title = "Item",
            type = Type.Anime,
            coverImage = "",
            posterImage = "",
            isTracked = false,
            isPendingTrack = false,
        )
    )
    DetailPane(
        detailState = state,
        showBack = true,
        trackItem = {},
        goBack = {}
    )
}
