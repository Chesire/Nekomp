package com.chesire.nekomp.feature.library.ui.pane

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.chesire.nekomp.feature.library.ui.Entry
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun ListItemsPane(
    entries: ImmutableList<Entry>,
    modifier: Modifier = Modifier,
    onEntryClick: (Entry) -> Unit
) {
}
