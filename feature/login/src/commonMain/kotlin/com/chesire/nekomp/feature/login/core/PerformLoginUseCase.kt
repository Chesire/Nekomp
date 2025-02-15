package com.chesire.nekomp.feature.login.core

import com.chesire.nekomp.library.datasource.auth.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class PerformLoginUseCase(private val authRepository: AuthRepository) {

    suspend operator fun invoke(username: String, password: String): Result<Any> {
        return withContext(Dispatchers.IO) {
            authRepository.authenticate(username, password)
        }
    }
}
