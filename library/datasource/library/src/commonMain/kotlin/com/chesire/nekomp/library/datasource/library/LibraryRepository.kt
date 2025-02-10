package com.chesire.nekomp.library.datasource.library

import com.chesire.nekomp.library.datasource.library.remote.LibraryApi
import com.chesire.nekomp.library.datasource.user.UserRepository

class LibraryRepository(
    private val libraryApi: LibraryApi,
    private val userRepository: UserRepository
) {

    suspend fun retrieve(): Result<String> {
        return libraryApi.retrieve(userRepository.user.id)
    }
}
