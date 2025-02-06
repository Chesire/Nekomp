package com.chesire.nekomp.core.network

sealed class Either<T> {
    data class Success<T>(val data: T) : Either<T>()
    class Error(val ex: Throwable) : Either<Nothing>()

    companion object {
        fun <T> success(data: T) = Success(data)
        fun error(ex: Throwable) = Error(ex)
    }
}
