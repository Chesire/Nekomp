package com.chesire.nekomp.feature.library.ui.pane

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chesire.nekomp.core.ui.component.series.SeriesCardItem
import com.chesire.nekomp.feature.library.ui.Entry
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun CardItemsPane(
    entries: ImmutableList<Entry>,
    modifier: Modifier = Modifier,
    onEntryClick: (Entry) -> Unit,
    onPlusOneClick: (Entry) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(300.dp),
        modifier = modifier.padding(horizontal = 16.dp),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = entries,
            key = { it.entryId }
        ) {
            SeriesCardItem(
                title = it.title,
                posterImage = it.posterImage,
                progress = it.progressDisplay,
                progressPercent = it.progressPercent,
                isUpdating = it.isUpdating,
                modifier = modifier.animateItem(),
                showPlusButton = it.canUpdate,
                onClick = { onEntryClick(it) },
                onPlusClick = { onPlusOneClick(it) }
            )
        }
    }
}
