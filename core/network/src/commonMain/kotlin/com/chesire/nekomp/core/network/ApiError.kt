package com.chesire.nekomp.core.network

data class ApiError(
    val code: Int,
    val body: String,
    val reason: String = ""
) : Throwable()
