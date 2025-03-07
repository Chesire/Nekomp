@file:OptIn(ExperimentalMaterial3Api::class)

package com.chesire.nekomp.feature.library.ui.pane

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import com.chesire.nekomp.core.resources.NekoRes
import com.chesire.nekomp.feature.library.ui.Entry
import nekomp.core.resources.generated.resources.nav_content_description_go_back
import org.jetbrains.compose.resources.stringResource

@Composable
fun DetailPane(
    entry: Entry?,
    showBack: Boolean,
    goBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = entry?.title ?: "")
                },
                navigationIcon = {
                    if (showBack) {
                        IconButton(onClick = goBack) {
                            Icon(
                                painter = rememberVectorPainter(Icons.AutoMirrored.Filled.ArrowBack),
                                contentDescription = stringResource(
                                    NekoRes.string.nav_content_description_go_back
                                )
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (entry != null) {
            Column(modifier = Modifier.padding(paddingValues)) {
                Text("Test")
            }
        }
    }
}
