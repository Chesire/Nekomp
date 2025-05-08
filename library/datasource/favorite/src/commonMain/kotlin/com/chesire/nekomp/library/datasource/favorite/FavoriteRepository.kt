package com.chesire.nekomp.library.datasource.favorite

import co.touchlab.kermit.Logger
import com.chesire.nekomp.library.datasource.favorite.remote.FavoriteApi
import com.chesire.nekomp.library.datasource.kitsumodels.toImage
import com.chesire.nekomp.library.datasource.user.UserRepository
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
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

    suspend fun retrieveAnimeFavorites(): Result<Unit, Unit> {
        val user = userRepository.user.firstOrNull()
        if (user?.isAuthenticated != true) {
            Logger.e("LibraryRepository") { "No user object, cancelling retrieve" }
            return Err(Unit) // TODO: Add custom error type
        }

        val response = favoriteApi.retrieveFavoriteAnime(user.id)
        Logger.d("Result - $response")
        return Ok(Unit)
    }

    suspend fun retrieveMangaFavorites(): Result<Unit, Unit> {
        val user = userRepository.user.firstOrNull()
        if (user?.isAuthenticated != true) {
            Logger.e("LibraryRepository") { "No user object, cancelling retrieve" }
            return Err(Unit) // TODO: Add custom error type
        }

        val response = favoriteApi.retrieveFavoriteManga(user.id)
        Logger.d("Result - $response")
        return Ok(Unit)
    }
}
