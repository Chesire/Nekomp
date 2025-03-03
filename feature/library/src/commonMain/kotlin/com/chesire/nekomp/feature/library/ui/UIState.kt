package com.chesire.nekomp.feature.library.ui

import com.chesire.nekomp.feature.library.data.ViewType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class UIState(
    val entries: ImmutableList<Entry> = persistentListOf(),
    val viewType: ViewType = ViewType.Card,
    val bottomSheet: LibraryBottomSheet? = null,
    val viewEvent: ViewEvent? = null,
)

sealed interface LibraryBottomSheet {

    data class ViewTypeBottomSheet(
        val types: ImmutableList<ViewType>,
        val selectedType: ViewType
    ) : LibraryBottomSheet
}

data class Entry(
    val id: Int,
    val title: String,
    val image: String
)
