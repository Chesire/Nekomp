package com.chesire.nekomp.core.ui.component.series

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlusOne
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.chesire.nekomp.core.resources.NekoRes
import nekomp.core.resources.generated.resources.card_item_plus_one
import org.jetbrains.compose.resources.stringResource

@Composable
fun SeriesCardItem(
    title: String,
    posterImage: String,
    progress: String,
    progressPercent: Float,
    isUpdating: Boolean,
    modifier: Modifier = Modifier,
    showPlusButton: Boolean = true,
    onClick: () -> Unit,
    onPlusClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier
            .widthIn(max = 360.dp)
            .padding(8.dp)
    ) {
        Row {
            AsyncImage(
                model = posterImage,
                contentDescription = null,
                modifier = Modifier.requiredHeight(200.dp)
            )
            Column(modifier = Modifier.requiredHeight(200.dp)) {
                Text(
                    text = title,
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                )
                Spacer(Modifier.weight(1f))
                Row(
                    modifier = Modifier.padding(start = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = progress,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (showPlusButton) {
                        IconButton(
                            onClick = onPlusClick,
                            enabled = !isUpdating
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlusOne,
                                contentDescription = stringResource(NekoRes.string.card_item_plus_one)
                            )
                        }
                    }
                }
                LinearProgressIndicator(
                    progress = { progressPercent },
                    modifier = Modifier.height(4.dp),
                    gapSize = 0.dp,
                    drawStopIndicator = {}
                )
            }
        }
    }
}
