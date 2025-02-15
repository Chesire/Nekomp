package com.chesire.nekomp.core.network

sealed class NetworkError : Throwable() {
    data class Generic(val rethrown: Throwable) : NetworkError()
    data class Api(
        val code: Int,
        val body: String,
        val reason: String = ""
    ) : NetworkError()
}
