package com.chesire.nekomp.feature.library.ui.pane

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlusOne
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.chesire.nekomp.feature.library.ui.Entry
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun ListItemsPane(
    entries: ImmutableList<Entry>,
    modifier: Modifier = Modifier,
    onEntryClick: (Entry) -> Unit,
    onPlusOneClick: (Entry) -> Unit
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
            key = { it.entryId }
        ) {
            ListItem(
                entry = it,
                modifier = Modifier.animateItem(),
                onEntryClick = onEntryClick,
                onPlusOneClick = onPlusOneClick
            )
        }
    }
}

@Composable
private fun ListItem(
    entry: Entry,
    modifier: Modifier = Modifier,
    onEntryClick: (Entry) -> Unit,
    onPlusOneClick: (Entry) -> Unit
) {
    ElevatedCard(
        onClick = { onEntryClick(entry) },
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Box(modifier = Modifier.height(56.dp)) {
            AsyncImage(
                model = entry.coverImage,
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                alpha = 0.4f
            )
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = entry.title,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                OutlinedIconButton(onClick = { onPlusOneClick(entry) }) {
                    Icon(
                        imageVector = Icons.Default.PlusOne,
                        contentDescription = null // TODO: Add content description
                    )
                }
            }
        }
    }
}
