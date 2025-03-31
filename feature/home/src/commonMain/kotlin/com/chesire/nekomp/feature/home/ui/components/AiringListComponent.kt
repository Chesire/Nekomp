package com.chesire.nekomp.feature.home.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.chesire.nekomp.feature.home.ui.AiringItem
import kotlinx.collections.immutable.ImmutableList

@Composable
fun AiringListComponent(
    airingItems: ImmutableList<AiringItem>,
    onAiringItemClick: (AiringItem) -> Unit
) {
    Column {
        Text("Coming up")
        LazyRow(
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = airingItems,
                key = { it.entryId }
            ) {
                AiringItemComponent(
                    airingItem = it,
                    onAiringItemClick = onAiringItemClick,
                    modifier = Modifier.animateItem()
                )
            }
        }
    }
}

@Composable
private fun AiringItemComponent(
    airingItem: AiringItem,
    onAiringItemClick: (AiringItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onAiringItemClick(airingItem) },
        modifier = modifier.width(280.dp).height(80.dp),
    ) {
        Box {
            AsyncImage(
                model = airingItem.posterImage,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillBounds
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            .5F to Color.Transparent,
                            .7f to Color.Black.copy(alpha = 0.5f),
                            .8f to Color.Black.copy(alpha = 0.9f),
                            1F to Color.Black.copy(alpha = 1f)
                        )
                    )
            )
            Text(
                text = airingItem.airingAt,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}
