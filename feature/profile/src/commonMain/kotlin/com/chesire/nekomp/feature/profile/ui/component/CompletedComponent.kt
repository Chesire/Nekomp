package com.chesire.nekomp.feature.profile.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.chesire.nekomp.core.resources.NekoRes
import com.chesire.nekomp.feature.profile.ui.CompletedData
import nekomp.core.resources.generated.resources.profile_completed_completed_anime
import nekomp.core.resources.generated.resources.profile_completed_completed_manga
import org.jetbrains.compose.resources.stringResource

@Composable
fun CompletedComponent(completedData: CompletedData) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        CompletedSection(
            title = stringResource(NekoRes.string.profile_completed_completed_anime),
            progress = completedData.animeProgress,
            percent = completedData.animePercent
        )
        CompletedSection(
            title = stringResource(NekoRes.string.profile_completed_completed_manga),
            progress = completedData.mangaProgress,
            percent = completedData.mangaPercent
        )
    }
}

@Composable
private fun CompletedSection(title: String, progress: String, percent: Float) {
    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = progress,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.End
            )
        }
        LinearProgressIndicator(
            progress = { percent },
            modifier = Modifier.fillMaxWidth(),
            drawStopIndicator = {}
        )
    }
}
