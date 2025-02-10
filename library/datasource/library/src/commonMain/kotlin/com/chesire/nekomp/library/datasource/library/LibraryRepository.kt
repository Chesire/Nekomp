package com.chesire.nekomp.library.datasource.library

import com.chesire.nekomp.library.datasource.library.remote.LibraryApi

class LibraryRepository(private val libraryApi: LibraryApi) {

    suspend fun retrieve(): Result<String> {
        TODO("Need the user ID")
        return libraryApi.retrieve(0)
            .onSuccess {
                val s = ""
            }
            .onFailure {
                val f = ""
            }
    }
}
