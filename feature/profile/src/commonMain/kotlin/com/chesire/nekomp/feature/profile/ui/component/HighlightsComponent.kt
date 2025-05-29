package com.chesire.nekomp.feature.profile.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.chesire.nekomp.core.resources.NekoRes
import com.chesire.nekomp.feature.profile.ui.HighlightsData
import nekomp.core.resources.generated.resources.profile_highlights_chapters_read
import nekomp.core.resources.generated.resources.profile_highlights_episodes_watched
import nekomp.core.resources.generated.resources.profile_highlights_series_completed
import nekomp.core.resources.generated.resources.profile_highlights_time_spent_watching
import org.jetbrains.compose.resources.stringResource

@Composable
fun HighlightsComponent(highlightsData: HighlightsData) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        shape = RoundedCornerShape(8.dp)
    ) {
        HighlightsRow(
            title1 = stringResource(NekoRes.string.profile_highlights_episodes_watched),
            text1 = highlightsData.episodesWatched,
            title2 = stringResource(NekoRes.string.profile_highlights_chapters_read),
            text2 = highlightsData.chaptersRead,
            modifier = Modifier.weight(1f)
        )
        HorizontalDivider()
        HighlightsRow(
            title1 = stringResource(NekoRes.string.profile_highlights_time_spent_watching),
            text1 = highlightsData.timeSpentWatching,
            title2 = stringResource(NekoRes.string.profile_highlights_series_completed),
            text2 = highlightsData.seriesCompleted,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun HighlightsRow(
    title1: String,
    text1: String,
    title2: String,
    text2: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        HighlightsSection(
            title = title1,
            text = text1,
            modifier = Modifier.weight(1f)
        )
        VerticalDivider()
        HighlightsSection(
            title = title2,
            text = text2,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun HighlightsSection(
    title: String,
    text: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .heightIn(min = 120.dp)
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center
        )
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
    }
}
