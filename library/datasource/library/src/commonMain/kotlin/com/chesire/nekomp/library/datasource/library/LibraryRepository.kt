package com.chesire.nekomp.library.datasource.library

import com.chesire.nekomp.library.datasource.library.remote.LibraryApi
import com.chesire.nekomp.library.datasource.user.UserRepository

class LibraryRepository(
    private val libraryApi: LibraryApi,
    private val userRepository: UserRepository
) {

    suspend fun retrieve(): Result<String> {
        return libraryApi.retrieveAnime(userRepository.user.id, 0, 20)
            .map {
                "Test"
            }
            .recover {
                val ex = it
                "Test"
            }
            .onSuccess {
                // save
            }
    }
}
