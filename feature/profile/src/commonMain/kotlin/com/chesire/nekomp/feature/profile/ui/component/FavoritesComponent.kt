package com.chesire.nekomp.feature.profile.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.chesire.nekomp.core.resources.NekoRes
import com.chesire.nekomp.feature.profile.ui.FavoritesData
import kotlinx.collections.immutable.ImmutableList
import nekomp.core.resources.generated.resources.profile_favorites_favorite_anime
import nekomp.core.resources.generated.resources.profile_favorites_favorite_character
import nekomp.core.resources.generated.resources.profile_favorites_favorite_manga
import org.jetbrains.compose.resources.stringResource

@Composable
fun FavoritesComponent(favoritesData: FavoritesData) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        if (favoritesData.favoriteCharacters.isNotEmpty()) {
            FavoriteSection(
                title = stringResource(NekoRes.string.profile_favorites_favorite_character),
                listData = favoritesData.favoriteCharacters
            )
        }
        if (favoritesData.favoriteAnime.isNotEmpty()) {
            FavoriteSection(
                title = stringResource(NekoRes.string.profile_favorites_favorite_anime),
                listData = favoritesData.favoriteAnime
            )
        }
        if (favoritesData.favoriteManga.isNotEmpty()) {
            FavoriteSection(
                title = stringResource(NekoRes.string.profile_favorites_favorite_manga),
                listData = favoritesData.favoriteManga
            )
        }
    }
}

@Composable
private fun FavoriteSection(title: String, listData: ImmutableList<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 16.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        LazyRow(
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listData) { item ->
                AsyncImage(
                    model = item,
                    contentDescription = null,
                    modifier = Modifier.clip(RoundedCornerShape(4.dp))
                )
            }
        }
    }
}
