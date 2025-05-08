package com.chesire.nekomp.library.datasource.favorite

import co.touchlab.kermit.Logger
import com.chesire.nekomp.library.datasource.favorite.remote.FavoriteApi
import com.chesire.nekomp.library.datasource.user.UserRepository
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import kotlinx.coroutines.flow.firstOrNull

class FavoriteRepository(
    private val favoriteApi: FavoriteApi,
    private val userRepository: UserRepository // TODO: Inject method to get the user id?
) {

    suspend fun debugcall(): Result<Unit, Unit> {
        val user = userRepository.user.firstOrNull()
        if (user?.isAuthenticated != true) {
            Logger.e("LibraryRepository") { "No user object, cancelling retrieve" }
            return Err(Unit) // TODO: Add custom error type
        }
        favoriteApi.retrieveFavoriteCharacters(user.id)

        return Ok(Unit)
    }
}
