package com.chesire.nekomp.library.datasource.favorite

import co.touchlab.kermit.Logger
import com.chesire.nekomp.core.network.NetworkError
import com.chesire.nekomp.library.datasource.favorite.remote.FavoriteApi
import com.chesire.nekomp.library.datasource.favorite.remote.model.RetrieveFavoriteMediaResponseDto
import com.chesire.nekomp.library.datasource.kitsumodels.toImage
import com.chesire.nekomp.library.datasource.user.UserRepository
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onSuccess
import kotlinx.coroutines.flow.firstOrNull

class FavoriteRepository(
    private val favoriteApi: FavoriteApi,
    private val userRepository: UserRepository // TODO: Inject method to get the user id?
) {

    suspend fun retrieveCharacterFavorites(): Result<List<Favorite>, Unit> {
        val user = userRepository.user.firstOrNull()
        if (user?.isAuthenticated != true) {
            Logger.e("LibraryRepository") { "No user object, cancelling retrieve" }
            return Err(Unit) // TODO: Add custom error type
        }

        val response = favoriteApi.retrieveFavoriteCharacters(user.id)
        return if (response.isOk) {
            val characterMap = response.value.included?.associateBy { it.id } ?: emptyMap()
            Ok(
                response.value.data.mapNotNull { favorite ->
                    val charId = favorite.relationships.item?.data?.id
                    val character = characterMap[charId]
                    if (character != null) {
                        Favorite(
                            type = FavoriteType.Character,
                            title = character.attributes.canonicalName,
                            image = character.attributes.image.toImage(),
                            rank = favorite.attributes.favRank
                        )
                    } else {
                        null
                    }
                }
            )
        } else {
            Err(Unit)
        }
    }

    suspend fun retrieveAnimeFavorites(): Result<List<Favorite>, Unit> {
        val user = userRepository.user.firstOrNull()
        if (user?.isAuthenticated != true) {
            Logger.e("LibraryRepository") { "No user object, cancelling retrieve" }
            return Err(Unit) // TODO: Add custom error type
        }

        val response = favoriteApi.retrieveFavoriteAnime(user.id)
        return parseMediaFavorites(
            response = response,
            type = FavoriteType.Anime
        ).onSuccess {
            // TODO: store in storage
        }
    }

    suspend fun retrieveMangaFavorites(): Result<List<Favorite>, Unit> {
        val user = userRepository.user.firstOrNull()
        if (user?.isAuthenticated != true) {
            Logger.e("LibraryRepository") { "No user object, cancelling retrieve" }
            return Err(Unit) // TODO: Add custom error type
        }

        val response = favoriteApi.retrieveFavoriteManga(user.id)
        return parseMediaFavorites(
            response = response,
            type = FavoriteType.Manga
        ).onSuccess {
            // TODO: store in storage
        }
    }

    private fun parseMediaFavorites(
        response: Result<RetrieveFavoriteMediaResponseDto, NetworkError>,
        type: FavoriteType
    ): Result<List<Favorite>, Unit> {
        return if (response.isOk) {
            val mediaMap = response.value.included?.associateBy { it.id } ?: emptyMap()
            Ok(
                response.value.data.mapNotNull { favorite ->
                    val id = favorite.relationships.item?.data?.id
                    val media = mediaMap[id]
                    if (media != null) {
                        Favorite(
                            type = type,
                            title = media.attributes.canonicalTitle,
                            image = media.attributes.posterImage.toImage(),
                            rank = favorite.attributes.favRank
                        )
                    } else {
                        null
                    }
                }
            )
        } else {
            Err(Unit)
        }
    }
}
