package com.chesire.nekomp.feature.discover.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp

@Composable
internal fun DetailPane(
    item: DiscoverItem?,
    showBack: Boolean,
    goBack: () -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (item != null) {
            if (showBack) {
                IconButton(
                    onClick = goBack,
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Icon(
                        painter = rememberVectorPainter(Icons.AutoMirrored.Filled.ArrowBack),
                        contentDescription = "Go back"
                    )
                }
            }
        } else {
            Text("No entry selected")
        }
    }
}
