@file:OptIn(ExperimentalMaterial3Api::class)

package com.chesire.nekomp.feature.library.ui.pane

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.ElevatedCard
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
import coil3.compose.AsyncImage
import com.chesire.nekomp.feature.library.data.LibraryViewType
import com.chesire.nekomp.feature.library.ui.Entry
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ListPane(
    entries: ImmutableList<Entry>,
    currentViewType: LibraryViewType,
    onEntryClick: (Entry) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Anime")
                },
                actions = {
                    IconButton(onClick = {}) {
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
            LibraryViewType.List -> ListView(
                entries = entries,
                modifier = Modifier.padding(paddingValues),
                onEntryClick = onEntryClick
            )

            LibraryViewType.Card -> ListView(
                entries = entries,
                modifier = Modifier.padding(paddingValues),
                onEntryClick = onEntryClick
            )

            LibraryViewType.Grid -> ListView(
                entries = entries,
                modifier = Modifier.padding(paddingValues),
                onEntryClick = onEntryClick
            )
        }
    }
}

@Composable
private fun ListView(
    entries: ImmutableList<Entry>,
    modifier: Modifier = Modifier,
    onEntryClick: (Entry) -> Unit
) {
}

@Composable
private fun CardView(
    entries: ImmutableList<Entry>,
    modifier: Modifier = Modifier,
    onEntryClick: (Entry) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .animateContentSize(),
        horizontalAlignment = Alignment.Start,
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = entries,
            key = { it.id }
        ) {
            ListItem(
                entry = it,
                modifier = Modifier.animateItem(),
                onEntryClick = onEntryClick
            )
        }
    }
}

@Composable
private fun GridView(
    entries: ImmutableList<Entry>,
    modifier: Modifier = Modifier,
    onEntryClick: (Entry) -> Unit
) {

}

@Composable
private fun ListItem(
    entry: Entry,
    modifier: Modifier = Modifier,
    onEntryClick: (Entry) -> Unit
) {
    ElevatedCard(
        onClick = { onEntryClick(entry) },
        modifier = modifier
            .widthIn(max = 360.dp)
            .padding(8.dp)
    ) {
        Row {
            AsyncImage(
                model = entry.image,
                contentDescription = null,
                modifier = Modifier.requiredHeight(200.dp)
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = entry.title,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
@Preview
private fun Preview() {
    ListPane(
        entries = persistentListOf<Entry>(
            Entry(0, "Title1", "")
        ),
        currentViewType = LibraryViewType.Card,
        onEntryClick = {}
    )
}
