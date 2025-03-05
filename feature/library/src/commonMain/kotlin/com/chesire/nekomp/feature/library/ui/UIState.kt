package com.chesire.nekomp.feature.library.ui

import androidx.compose.runtime.Stable
import com.chesire.nekomp.feature.library.data.SortChoice
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

    // TODO: Filter

    data class ViewTypeBottomSheet(
        val types: ImmutableList<ViewType>,
        val selectedType: ViewType
    ) : LibraryBottomSheet

    data class SortBottomSheet(
        val options: ImmutableList<SortChoice>,
        val selectedOption: SortChoice
    ) : LibraryBottomSheet
}

@Stable
data class Entry(
    val entryId: Int,
    val title: String,
    val posterImage: String,
    val coverImage: String,
    val progressPercent: Float,
    val progress: Int
)
