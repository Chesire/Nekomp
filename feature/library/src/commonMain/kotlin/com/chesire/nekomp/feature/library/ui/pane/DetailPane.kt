@file:OptIn(ExperimentalMaterial3Api::class)

package com.chesire.nekomp.feature.library.ui.pane

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                SubcomposeAsyncImage(
                    model = entry.coverImage,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().heightIn(min = 120.dp),
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

            Text("Test")
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
                isUpdating = false,
                canUpdate = true
            ),
            showBack = true,
            goBack = {}
        )
    }
}
