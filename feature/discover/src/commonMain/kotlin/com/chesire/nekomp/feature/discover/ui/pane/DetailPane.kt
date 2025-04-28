@file:OptIn(ExperimentalMaterial3Api::class)

package com.chesire.nekomp.feature.discover.ui.pane

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material.icons.filled.LibraryAddCheck
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.chesire.nekomp.core.ext.capitalize
import com.chesire.nekomp.core.model.Type
import com.chesire.nekomp.core.resources.NekoRes
import com.chesire.nekomp.core.ui.NekompTheme
import com.chesire.nekomp.core.ui.drawable.AniListLogo
import com.chesire.nekomp.core.ui.drawable.KitsuLogo
import com.chesire.nekomp.core.ui.drawable.MalLogo
import com.chesire.nekomp.core.ui.util.NoRippleInteractionSource
import com.chesire.nekomp.core.ui.util.ifFalse
import com.chesire.nekomp.feature.discover.ui.DetailState
import com.chesire.nekomp.feature.discover.ui.DiscoverItem
import com.chesire.nekomp.feature.discover.ui.ViewAction
import com.chesire.nekomp.feature.discover.ui.WebViewType
import kotlin.math.absoluteValue
import nekomp.core.resources.generated.resources.nav_content_description_go_back
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

private const val BASE_SYNOPSIS_LINES = 5

@Composable
internal fun DetailPane(
    detailState: DetailState,
    showBack: Boolean,
    execute: (ViewAction) -> Unit,
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
        Box {
            if (detailState.currentItem != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        // add startdate-enddate (if known)
                        // age rating?
                        InfoChip(
                            text = detailState.currentItem.type.name.capitalize(),
                            color = NekompTheme.colors.green
                        )
                        InfoChip(
                            text = detailState.currentItem.subType.capitalize(),
                            color = NekompTheme.colors.blue
                        )
                        InfoChip(
                            text = detailState.currentItem.status.capitalize(),
                            color = NekompTheme.colors.red
                        )
                        if (detailState.currentItem.totalLength != -1) {
                            InfoChip(
                                text = "${detailState.currentItem.totalLength} ${if (detailState.currentItem.type == Type.Anime) "episodes" else "chapters"}",
                                color = NekompTheme.colors.red
                            )
                        }
                        InfoChip(
                            text = detailState.currentItem.averageRating,
                            color = NekompTheme.colors.yellow
                        )
                    }
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        if (detailState.currentItem.isTracked) {
                            ActionBlock(
                                imageVector = Icons.Default.LibraryAdd,
                                helperText = "In library",
                                useColor = true,
                                onClick = {
                                    execute(ViewAction.TrackItemClick(detailState.currentItem))
                                }
                            )
                        } else {
                            ActionBlock(
                                imageVector = Icons.Default.LibraryAddCheck,
                                helperText = "Add to library",
                                useColor = false,
                                onClick = {
                                    execute(ViewAction.UntrackItemClick(detailState.currentItem))
                                }
                            )
                        }
                        ActionBlock(
                            imageVector = KitsuLogo,
                            helperText = "View on Kitsu",
                            useColor = false,
                            onClick = {
                                execute(
                                    ViewAction.WebViewClick(
                                        detailState.currentItem,
                                        WebViewType.Kitsu
                                    )
                                )
                            }
                        )
                        if (detailState.currentItem.malId != null) {
                            ActionBlock(
                                imageVector = MalLogo,
                                helperText = "View on MAL",
                                useColor = false,
                                onClick = {
                                    execute(
                                        ViewAction.WebViewClick(
                                            detailState.currentItem,
                                            WebViewType.MyAnimeList
                                        )
                                    )
                                }
                            )

                        }
                        if (detailState.currentItem.aniListId != null) {
                            ActionBlock(
                                imageVector = AniListLogo,
                                helperText = "View on AniList",
                                useColor = false,
                                onClick = {
                                    execute(
                                        ViewAction.WebViewClick(
                                            detailState.currentItem,
                                            WebViewType.AniList
                                        )
                                    )
                                }
                            )
                        }
                    }
                    Synopsis(detailState.currentItem.synopsis)
                }
            } else {
                Text("No entry selected")
            }
        }
    }
}

@Composable
private fun InfoChip(text: String, color: Color) {
    ElevatedSuggestionChip(
        onClick = {},
        label = {
            Text(text = text)
        },
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = color
        ),
        shape = RoundedCornerShape(32.dp),
        interactionSource = NoRippleInteractionSource()
    )
}

@Composable
private fun ActionBlock(
    imageVector: ImageVector,
    helperText: String,
    useColor: Boolean,
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = imageVector,
                contentDescription = helperText,
                modifier = Modifier.size(32.dp),
                tint = if (useColor) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
        }
        Text(
            text = helperText,
            color = if (useColor) MaterialTheme.colorScheme.primary else Color.Unspecified
        )
    }
}

@Composable
private fun Synopsis(text: String) {
    var isTextExpand by remember {
        mutableStateOf(false)
    }
    var didOverflowHeight by remember {
        mutableStateOf(false)
    }
    var lines by remember {
        mutableStateOf(BASE_SYNOPSIS_LINES)
    }

    Column(modifier = Modifier.animateContentSize()) {
        Box(modifier = Modifier.height(IntrinsicSize.Min)) {
            Text(
                text = text,
                maxLines = when (isTextExpand) {
                    false -> BASE_SYNOPSIS_LINES
                    else -> Int.MAX_VALUE
                },
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .clickable(
                        onClick = {
                            isTextExpand = !isTextExpand
                        },
                        interactionSource = NoRippleInteractionSource(),
                        indication = null
                    ),
                onTextLayout = { textLayoutResult: TextLayoutResult ->
                    didOverflowHeight = textLayoutResult.didOverflowHeight
                    lines = textLayoutResult.lineCount
                }
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .ifFalse(isTextExpand && !didOverflowHeight) {
                        background(
                            brush = Brush.verticalGradient(
                                .5F to Color.Transparent,
                                .7f to MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                                .8f to MaterialTheme.colorScheme.background.copy(alpha = 0.9f),
                                1F to MaterialTheme.colorScheme.background.copy(alpha = 1f)
                            )
                        )
                    }
            )
        }
        if (lines > BASE_SYNOPSIS_LINES || didOverflowHeight) {
            IconButton(
                onClick = { isTextExpand = !isTextExpand },
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 32.dp)
            ) {
                if (lines <= BASE_SYNOPSIS_LINES) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Show full synopsis"
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Hide full synopsis"
                    )
                }
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
            subType = "OVA",
            status = "current",
            synopsis = "This is a synopsis of an anime series",
            averageRating = "81.13",
            totalLength = 12,
            coverImage = "",
            posterImage = "",
            isTracked = false,
            isPendingTrack = false
        )
    )
    DetailPane(
        detailState = state,
        showBack = true,
        execute = {},
        goBack = {}
    )
}
