package com.chesire.nekomp.feature.login.core

import com.chesire.nekomp.library.datasource.auth.AuthFailure
import com.chesire.nekomp.library.datasource.auth.AuthRepository
import com.chesire.nekomp.library.datasource.favorite.FavoriteRepository
import com.chesire.nekomp.library.datasource.stats.StatsRepository
import com.chesire.nekomp.library.datasource.user.UserRepository
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class PerformLoginUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val favoriteRepository: FavoriteRepository,
    private val statsRepository: StatsRepository
) {

    // For now this class will send a call to get everything once the user has been retrieved
    // when the login flow is made nicer rewrite this.

    suspend operator fun invoke(username: String, password: String): Result<Any, AuthFailure> {
        return withContext(Dispatchers.IO) {
            authRepository
                .authenticate(username, password)
                .andThen {
                    userRepository
                        .retrieve()
                        .mapError { AuthFailure.BadRequest }
                        .onSuccess {
                            awaitAll(
                                async { favoriteRepository.retrieveAnimeFavorites() },
                                async { favoriteRepository.retrieveMangaFavorites() },
                                async { favoriteRepository.retrieveCharacterFavorites() },
                                async { statsRepository.retrieveConsumedStats() }
                            )
                        }
                }
        }
    }
}
