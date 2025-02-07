package com.chesire.nekomp.core.network

data class ApiError(
    val code: Int,
    val reason: String = "",
    val body: String
) : Throwable()
