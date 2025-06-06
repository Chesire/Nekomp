package com.chesire.nekomp.feature.discover.ui.pane

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun SearchPane(
    recentSearches: ImmutableList<String>,
    onRecentClicked: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .animateContentSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(
            items = recentSearches
        ) {
            Text(
                text = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onRecentClicked(it) }
                    .animateItem()
            )
        }
    }
}

@Composable
@Preview
private fun Preview() {
    SearchPane(
        recentSearches = persistentListOf("Search1", "Search2"),
        onRecentClicked = {}
    )
}
