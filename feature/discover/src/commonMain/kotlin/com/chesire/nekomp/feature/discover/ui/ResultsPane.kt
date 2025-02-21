package com.chesire.nekomp.feature.discover.ui

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun ResultsPane(searchResults: ImmutableList<SearchItem>) {
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(
            items = searchResults,
            key = { it.id }
        ) {
            Card {
                Text(text = it.title)
            }
        }
    }
}
