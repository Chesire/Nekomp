package com.chesire.nekomp.core.ui.component.series

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.chesire.nekomp.core.ui.util.transparentBacking

@Composable
fun SeriesAiringItem(
    text: String,
    coverImage: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .width(280.dp)
            .height(80.dp)
    ) {
        Box {
            AsyncImage(
                model = coverImage,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Box(modifier = Modifier.transparentBacking())
            Text(
                text = text,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}
