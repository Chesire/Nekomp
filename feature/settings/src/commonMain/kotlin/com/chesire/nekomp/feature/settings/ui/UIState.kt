package com.chesire.nekomp.feature.settings.ui

data class UIState(
    val rateCheckbox: Boolean = false,
    val version: String = "",
    val viewEvent: ViewEvent? = null
)
