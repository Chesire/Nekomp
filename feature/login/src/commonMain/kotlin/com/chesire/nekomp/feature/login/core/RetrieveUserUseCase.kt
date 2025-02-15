package com.chesire.nekomp.feature.login.core

import com.chesire.nekomp.library.datasource.user.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class RetrieveUserUseCase(private val userRepository: UserRepository) {

    suspend operator fun invoke(): Result<Any> {
        return withContext(Dispatchers.IO) {
            userRepository.retrieve()
        }
    }
}
