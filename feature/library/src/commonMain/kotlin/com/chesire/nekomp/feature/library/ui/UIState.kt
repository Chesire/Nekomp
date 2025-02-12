package com.chesire.nekomp.feature.library.ui

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class UIState(
    val entries: ImmutableList<Entry> = persistentListOf(),
    val viewEvent: ViewEvent? = null
)

data class Entry(
    val title: String,
    val image: String
)
