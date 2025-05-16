package com.chesire.nekomp.library.datasource.favorite.local

import com.chesire.nekomp.core.database.dao.FavoriteDao
import com.chesire.nekomp.core.database.entity.FavoriteEntity
import com.chesire.nekomp.core.model.Image
import com.chesire.nekomp.library.datasource.favorite.Favorite
import com.chesire.nekomp.library.datasource.favorite.FavoriteType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteStorage(private val favoriteDao: FavoriteDao) {

    val favoriteCharacters: Flow<List<Favorite>> = favoriteDao
        .entries()
        .map { entry ->
            entry
                .filter { FavoriteType.fromString(it.type) == FavoriteType.Character }
                .map { it.toFavorite(FavoriteType.Character) }
        }

    val favoriteAnime: Flow<List<Favorite>> = favoriteDao
        .entries()
        .map { entry ->
            entry
                .filter { FavoriteType.fromString(it.type) == FavoriteType.Anime }
                .map { it.toFavorite(FavoriteType.Anime) }
        }

    val favoriteManga: Flow<List<Favorite>> = favoriteDao
        .entries()
        .map { entry ->
            entry
                .filter { FavoriteType.fromString(it.type) == FavoriteType.Manga }
                .map { it.toFavorite(FavoriteType.Manga) }
        }

    suspend fun updateFavorites() {

    }

    private fun FavoriteEntity.toFavorite(type: FavoriteType): Favorite {
        return Favorite(
            type = type,
            title = title,
            image = Image(
                tiny = posterImageTiny,
                small = posterImageSmall,
                medium = posterImageMedium,
                large = posterImageLarge,
                original = posterImageOriginal
            ),
            rank = rank
        )
    }
}
