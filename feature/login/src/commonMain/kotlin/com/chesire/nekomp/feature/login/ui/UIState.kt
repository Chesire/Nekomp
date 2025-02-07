package com.chesire.nekomp.feature.login.ui

data class UIState(
    val email: String = "",
    val password: String = "",
    val isPendingLogin: Boolean = false,
    val viewEvent: ViewEvent? = null
)
