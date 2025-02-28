package com.chesire.nekomp.feature.home.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.LinkInteractionListener
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.chesire.nekomp.core.model.Type
import com.chesire.nekomp.feature.home.ui.TrendItem
import kotlinx.collections.immutable.ImmutableList

@Composable
fun TrendingListComponent(
    trendingAnime: ImmutableList<TrendItem>,
    trendingManga: ImmutableList<TrendItem>,
    onTrendItemClick: (TrendItem) -> Unit
) {
    var selectedType by rememberSaveable { mutableStateOf(Type.Anime) }
    val trendingText = buildAnnotatedString {
        val tag = if (selectedType == Type.Anime) "anime" else "manga"
        append("Trending ")
        withLink(
            link = LinkAnnotation.Clickable(
                tag = tag,
                styles = TextLinkStyles(
                    style = SpanStyle(color = MaterialTheme.colorScheme.secondary)
                ),
                linkInteractionListener = LinkInteractionListener {
                    selectedType = if (selectedType == Type.Anime) Type.Manga else Type.Anime
                }
            )
        ) {
            append(tag)
            append("⌄")
        }
    }
    Column {
        Text(text = trendingText)
        LazyRow(
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = if (selectedType == Type.Anime) trendingAnime else trendingManga,
                key = { it.id }
            ) {
                TrendItemComponent(
                    trendItem = it,
                    onTrendItemClick = onTrendItemClick
                )
            }
        }
    }
}

@Composable
private fun TrendItemComponent(
    trendItem: TrendItem,
    onTrendItemClick: (TrendItem) -> Unit
) {
    Column(
        modifier = Modifier.width(IntrinsicSize.Min),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            onClick = { onTrendItemClick(trendItem) },
            modifier = Modifier.wrapContentSize(),
        ) {
            AsyncImage(
                model = trendItem.posterImage,
                contentDescription = null
            )
        }
    }
}
