package com.chesire.nekomp.feature.login.core

import com.chesire.nekomp.library.datasource.auth.AuthFailure
import com.chesire.nekomp.library.datasource.auth.AuthRepository
import com.chesire.nekomp.library.datasource.user.UserRepository
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.mapError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class PerformLoginUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(username: String, password: String): Result<Any, AuthFailure> {
        return withContext(Dispatchers.IO) {
            authRepository
                .authenticate(username, password)
                .andThen {
                    userRepository
                        .retrieve()
                        .mapError { AuthFailure.BadRequest }
                }
        }
    }
}
