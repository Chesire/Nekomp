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
import com.chesire.nekomp.core.ui.component.series.SeriesGridItem
import com.chesire.nekomp.feature.home.ui.WatchItem
import kotlinx.collections.immutable.ImmutableList

@Composable
fun WatchListComponent(
    watchItems: ImmutableList<WatchItem>,
    onWatchItemClick: (WatchItem) -> Unit,
    onPlusOneClick: (WatchItem) -> Unit
) {
    Column {
        Text("Watch List")
        LazyRow(
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = watchItems,
                key = { it.entryId }
            ) {
                SeriesGridItem(
                    title = it.title,
                    backgroundImage = it.posterImage,
                    progress = it.progressDisplay,
                    progressPercent = it.progressPercent,
                    modifier = Modifier.animateItem(),
                    onClick = { onWatchItemClick(it) },
                    onPlusClick = { onPlusOneClick(it) }
                )
            }
        }
    }
}
