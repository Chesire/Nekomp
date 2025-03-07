package com.chesire.nekomp.feature.library.ui.pane

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlusOne
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.chesire.nekomp.feature.library.ui.Entry
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun CardItemsPane(
    entries: ImmutableList<Entry>,
    modifier: Modifier = Modifier,
    onEntryClick: (Entry) -> Unit,
    onPlusOneClick: (Entry) -> Unit
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.Start,
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = entries,
            key = { it.entryId }
        ) {
            CardItem(
                entry = it,
                modifier = Modifier.animateItem(),
                onEntryClick = onEntryClick,
                onPlusOneClick = onPlusOneClick
            )
        }
    }
}

@Composable
private fun CardItem(
    entry: Entry,
    modifier: Modifier = Modifier,
    onEntryClick: (Entry) -> Unit,
    onPlusOneClick: (Entry) -> Unit
) {
    ElevatedCard(
        onClick = { onEntryClick(entry) },
        modifier = modifier
            .widthIn(max = 360.dp)
            .padding(8.dp)
    ) {
        Row {
            AsyncImage(
                model = entry.posterImage,
                contentDescription = null,
                modifier = Modifier.requiredHeight(200.dp)
            )
            Column(modifier = Modifier.requiredHeight(200.dp)) {
                Text(
                    text = entry.title,
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                )
                Spacer(Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = entry.displayProgress)
                    OutlinedIconButton(onClick = { onPlusOneClick(entry) }) {
                        Icon(
                            imageVector = Icons.Default.PlusOne,
                            contentDescription = null // TODO: Add content description
                        )
                    }
                }
                LinearProgressIndicator(
                    progress = { entry.progressPercent },
                    modifier = Modifier.height(4.dp),
                    gapSize = 0.dp,
                    drawStopIndicator = {}
                )
            }
        }
    }
}
