@file:OptIn(ExperimentalMaterial3Api::class)

package com.chesire.nekomp.feature.library.ui.pane

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import com.chesire.nekomp.core.resources.NekoRes
import com.chesire.nekomp.core.ui.NekompTheme
import com.chesire.nekomp.feature.library.ui.Entry
import nekomp.core.resources.generated.resources.nav_content_description_go_back
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DetailPane(
    entry: Entry?,
    showBack: Boolean,
    goBack: () -> Unit
) {
    if (entry != null) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                SubcomposeAsyncImage(
                    model = entry.coverImage,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 120.dp),
                    contentScale = ContentScale.Crop,
                    loading = { ImageLoadingOrError() },
                    error = { ImageLoadingOrError() }
                )
                if (showBack) {
                    IconButton(
                        onClick = goBack,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 24.dp)
                    ) {
                        Icon(
                            painter = rememberVectorPainter(Icons.AutoMirrored.Filled.ArrowBack),
                            contentDescription = stringResource(
                                NekoRes.string.nav_content_description_go_back
                            )
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                    )
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                AsyncImage(
                    model = entry.posterImage,
                    contentDescription = null,
                    modifier = Modifier
                        .requiredHeight(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = entry.title,
                        maxLines = 2,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    DetailText(
                        icon = Icons.Default.StarRate,
                        text = "4.82 / 5" // TODO: Add rating
                    )
                    DetailText(
                        icon = Icons.Default.Star,
                        text = "Finished" // TODO: Series status
                    )
                    DetailText(
                        icon = Icons.Default.CalendarMonth,
                        text = entry.airingTimeFrame
                    )
                    // Can we get the season?
                }
            }

            ProgressCard(
                entry = entry,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp, bottom = 8.dp)
            ) {
                // TODO: Show bottom sheet allowing the progress to be updated
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DetailCard(
                    title = "Status",
                    body = "Completed",
                    modifier = Modifier.padding(start = 16.dp).weight(1f),
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AddTask,
                            contentDescription = null,
                            tint = NekompTheme.colors.yellow
                        )
                    }
                ) {
                    // TODO: Show status bottom sheet
                }
                DetailCard(
                    title = "Rating",
                    body = "0",
                    modifier = Modifier.padding(end = 16.dp).weight(1f),
                    icon = {
                        Icon(
                            imageVector = Icons.Default.StarOutline,
                            contentDescription = null,
                            tint = NekompTheme.colors.green
                        )
                    }
                ) {
                    // TODO: Show rating bottom sheet
                }
            }
        }
    }
}

@Composable
private fun ImageLoadingOrError() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray.copy(alpha = 0.2f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Movie,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = Color.Red.copy(alpha = 0.5f)
        )
    }
}

@Composable
private fun DetailText(icon: ImageVector, text: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun ProgressCard(
    entry: Entry,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Movie,
                    contentDescription = null,
                    tint = NekompTheme.colors.blue
                )
                Text(
                    text = "Progress",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${(entry.progressPercent * 100).toInt()}%",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = entry.progressDisplay,
                modifier = Modifier.padding(vertical = 4.dp),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            LinearProgressIndicator(
                progress = { entry.progressPercent },
                modifier = Modifier.fillMaxWidth(),
                drawStopIndicator = {}
            )
        }
    }
}

@Composable
private fun DetailCard(
    title: String,
    body: String,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon()
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = body,
                modifier = Modifier.padding(vertical = 4.dp),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
@Preview
private fun Preview() {
    NekompTheme {
        DetailPane(
            entry = Entry(
                entryId = 1,
                title = "Title",
                posterImage = "",
                coverImage = "",
                progressPercent = 0.5f,
                progress = 5,
                progressDisplay = "5/10",
                airingTimeFrame = "2025 - -",
                isUpdating = false,
                canUpdate = true
            ),
            showBack = true,
            goBack = {}
        )
    }
}
