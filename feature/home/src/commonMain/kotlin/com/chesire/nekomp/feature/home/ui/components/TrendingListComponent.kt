package com.chesire.nekomp.feature.home.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.chesire.nekomp.feature.home.ui.TrendItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch

@Composable
fun TrendingListComponent(
    trendingAll: ImmutableList<TrendItem>,
    trendingAnime: ImmutableList<TrendItem>,
    trendingManga: ImmutableList<TrendItem>,
    onTrendItemClick: (TrendItem) -> Unit
) {
    var selectedType by rememberSaveable { mutableStateOf(DisplayType.Anime) }
    Column {
        TrendItemHeading(selectedType = selectedType) {
            selectedType = it
        }
        LazyRow(
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = when (selectedType) {
                    DisplayType.All -> trendingAll
                    DisplayType.Anime -> trendingAnime
                    DisplayType.Manga -> trendingManga
                },
                key = { it.id }
            ) {
                TrendItemComponent(
                    trendItem = it,
                    onTrendItemClick = onTrendItemClick
                )
            }
        }
    }
}

@Composable
private fun TrendItemHeading(
    selectedType: DisplayType,
    onSelectedTypeChange: (DisplayType) -> Unit
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var textWidth by remember { mutableStateOf<Dp>(0.dp) }

    var currentRotation by remember { mutableStateOf(180f) }
    val rotation = remember { Animatable(currentRotation) }

    val trendingText = buildAnnotatedString {
        val tag = selectedType.displayText
        append("Trending ")
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
            append(tag)
        }
    }
    Box {
        Row(
            modifier = Modifier.clickable {
                isDropdownExpanded = true
                scope.launch {
                    rotation.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(500)
                    ) {
                        currentRotation = value
                    }
                }
            }
        ) {
            Text(
                text = trendingText,
                onTextLayout = {
                    with(density) {
                        textWidth = it.size.width.toDp()
                    }
                }
            )
            Icon(
                imageVector = Icons.Default.ArrowDropUp,
                contentDescription = null,
                modifier = Modifier.rotate(rotation.value),
                tint = MaterialTheme.colorScheme.secondary
            )
        }
        DropdownMenu(
            expanded = isDropdownExpanded,
            onDismissRequest = {
                isDropdownExpanded = false
                scope.launch {
                    rotation.animateTo(
                        targetValue = 180f,
                        animationSpec = tween(500)
                    ) {
                        currentRotation = value
                    }
                }
            },
            offset = DpOffset(x = textWidth, y = 0.dp)
        ) {
            DisplayType.entries.forEach {
                DropdownMenuItem(
                    text = { Text(it.displayText) },
                    onClick = {
                        onSelectedTypeChange(it)
                        isDropdownExpanded = false

                        scope.launch {
                            rotation.animateTo(
                                targetValue = 180f,
                                animationSpec = tween(500)
                            ) {
                                currentRotation = value
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun TrendItemComponent(
    trendItem: TrendItem,
    onTrendItemClick: (TrendItem) -> Unit
) {
    Column(
        modifier = Modifier.width(IntrinsicSize.Min),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            onClick = { onTrendItemClick(trendItem) },
            modifier = Modifier.wrapContentSize(),
        ) {
            AsyncImage(
                model = trendItem.posterImage,
                contentDescription = null
            )
        }
    }
}

private enum class DisplayType(val displayText: String) {
    All("all"),
    Anime("anime"),
    Manga("manga")
}
