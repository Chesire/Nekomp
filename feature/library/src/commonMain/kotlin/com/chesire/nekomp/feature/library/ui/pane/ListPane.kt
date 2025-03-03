@file:OptIn(ExperimentalMaterial3Api::class)

package com.chesire.nekomp.feature.library.ui.pane

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.chesire.nekomp.feature.library.data.ViewType
import com.chesire.nekomp.feature.library.ui.Entry
import com.chesire.nekomp.feature.library.ui.ViewAction
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ListPane(
    entries: ImmutableList<Entry>,
    currentViewType: ViewType,
    execute: (ViewAction) -> Unit,
    onEntryClick: (Entry) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Anime")
                },
                actions = {
                    IconButton(onClick = { execute(ViewAction.ViewTypeClick) }) {
                        Icon(
                            imageVector = currentViewType.icon,
                            contentDescription = "Change view"
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filter"
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Sort,
                            contentDescription = "Sort"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when (currentViewType) {
            ViewType.List -> ListItemsPane(
                entries = entries,
                modifier = Modifier.padding(paddingValues),
                onEntryClick = onEntryClick,
                onPlusOneClick = { execute(ViewAction.ItemPlusOneClick(it)) }
            )

            ViewType.Card -> CardItemsPane(
                entries = entries,
                modifier = Modifier.padding(paddingValues),
                onEntryClick = onEntryClick,
                onPlusOneClick = { execute(ViewAction.ItemPlusOneClick(it)) }
            )

            ViewType.Grid -> GridItemsPane(
                entries = entries,
                modifier = Modifier.padding(paddingValues),
                onEntryClick = onEntryClick,
                onPlusOneClick = { execute(ViewAction.ItemPlusOneClick(it)) }
            )
        }
    }
}

@Composable
@Preview
private fun Preview() {
    ListPane(
        entries = persistentListOf<Entry>(
            Entry(0, "Title1", "", "", 0f, 0)
        ),
        currentViewType = ViewType.Card,
        execute = {},
        onEntryClick = {}
    )
}
