package com.chesire.nekomp.feature.home.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chesire.nekomp.core.ui.component.series.SeriesAiringItem
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
                SeriesAiringItem(
                    text = it.airingAt,
                    coverImage = it.coverImage,
                    modifier = Modifier.animateItem(),
                    onClick = { onAiringItemClick(it) }
                )
            }
        }
    }
}
