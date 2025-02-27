package com.chesire.nekomp.feature.home.ui

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class UIState(
    val username: String = "",
    val watchList: ImmutableList<WatchItem> = persistentListOf(),
    val viewEvent: ViewEvent? = null
)

@Stable
data class WatchItem(
    val name: String,
    val coverImage: String
)
