package com.chesire.nekomp.core.network

sealed interface NetworkError {

    data class ApiError(val code: Int, val body: String) : NetworkError
    data class GenericError(val throwable: Throwable) : NetworkError
}
