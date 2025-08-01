package com.chesire.nekomp.feature.settings.core

fun interface LogoutExecutor {

    suspend operator fun invoke()
}
