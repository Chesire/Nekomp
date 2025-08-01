package com.chesire.nekomp.core.network

fun interface RefreshErrorExecutor {

    suspend operator fun invoke()
}
