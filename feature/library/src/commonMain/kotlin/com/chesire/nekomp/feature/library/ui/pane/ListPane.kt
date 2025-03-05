@file:OptIn(ExperimentalMaterial3Api::class)

package com.chesire.nekomp.feature.library.ui.pane

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chesire.nekomp.core.model.Type
import com.chesire.nekomp.feature.library.data.ViewType
import com.chesire.nekomp.feature.library.ui.Entry
import com.chesire.nekomp.feature.library.ui.ViewAction
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ListPane(
    typeFilters: ImmutableMap<Type, Boolean>,
    entries: ImmutableList<Entry>,
    currentViewType: ViewType,
    execute: (ViewAction) -> Unit,
    onEntryClick: (Entry) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Library") },
                actions = {
                    IconButton(onClick = { execute(ViewAction.ViewTypeClick) }) {
                        Icon(
                            imageVector = currentViewType.icon,
                            contentDescription = "Change view"
                        )
                    }
                    IconButton(onClick = { execute(ViewAction.SortClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Sort,
                            contentDescription = "Sort"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            // TODO: Hide on scroll down
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) {
                typeFilters.entries.forEach { typeFilter ->
                    ElevatedFilterChip(
                        selected = typeFilter.value,
                        onClick = { execute(ViewAction.TypeFilterClick(typeFilter.key)) },
                        label = { Text(text = typeFilter.key.name) }
                    )
                }
            }
            when (currentViewType) {
                ViewType.List -> ListItemsPane(
                    entries = entries,
                    onEntryClick = onEntryClick,
                    onPlusOneClick = { execute(ViewAction.ItemPlusOneClick(it)) }
                )

                ViewType.Card -> CardItemsPane(
                    entries = entries,
                    onEntryClick = onEntryClick,
                    onPlusOneClick = { execute(ViewAction.ItemPlusOneClick(it)) }
                )

                ViewType.Grid -> GridItemsPane(
                    entries = entries,
                    onEntryClick = onEntryClick,
                    onPlusOneClick = { execute(ViewAction.ItemPlusOneClick(it)) }
                )
            }
        }
    }
}

@Composable
@Preview
private fun Preview() {
    ListPane(
        typeFilters = persistentMapOf(),
        entries = persistentListOf<Entry>(
            Entry(0, "Title1", "", "", 0f, 0)
        ),
        currentViewType = ViewType.Card,
        execute = {},
        onEntryClick = {}
    )
}
