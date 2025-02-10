package com.chesire.nekomp.library.datasource.library

import com.chesire.nekomp.library.datasource.library.remote.LibraryApi
import com.chesire.nekomp.library.datasource.user.UserRepository

private const val LIMIT = 20

class LibraryRepository(
    private val libraryApi: LibraryApi,
    private val userRepository: UserRepository
) {

    suspend fun retrieve(): Result<String> {
        // For now retrieve 20 anime, it should cycle through getting all of them using pagination
        return libraryApi.retrieveAnime(userRepository.user.id, 0, LIMIT)
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
