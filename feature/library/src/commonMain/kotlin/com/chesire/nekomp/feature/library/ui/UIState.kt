package com.chesire.nekomp.feature.library.ui

import androidx.compose.runtime.Stable
import com.chesire.nekomp.core.model.EntryStatus
import com.chesire.nekomp.core.model.Type
import com.chesire.nekomp.feature.library.data.SortChoice
import com.chesire.nekomp.feature.library.data.ViewType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

data class UIState(
    val entries: ImmutableList<Entry> = persistentListOf(),
    val selectedEntry: Entry? = null,
    val viewType: ViewType = ViewType.Card,
    val typeFilters: ImmutableMap<Type, Boolean> = persistentMapOf(),
    val statusFilters: ImmutableMap<EntryStatus, Boolean> = persistentMapOf(),
    val bottomSheet: LibraryBottomSheet? = null,
    val viewEvent: ViewEvent? = null,
)

sealed interface LibraryBottomSheet {

    data class ViewTypeBottomSheet(
        val types: ImmutableList<ViewType>,
        val selectedType: ViewType
    ) : LibraryBottomSheet

    data class SortBottomSheet(
        val options: ImmutableList<SortChoice>,
        val selectedOption: SortChoice
    ) : LibraryBottomSheet

    data class ProgressBottomSheet(
        val entryId: Int,
        val currentProgress: Int,
        val maxProgress: Int?,
        val title: String,
        val type: Type,
        val state: BottomSheetState = BottomSheetState.Default
    ) : LibraryBottomSheet

    data class StatusBottomSheet(
        val entryId: Int,
        val currentStatus: EntryStatus,
        val allStatus: PersistentSet<EntryStatus>,
        val title: String,
        val state: BottomSheetState = BottomSheetState.Default
    ) : LibraryBottomSheet

    sealed interface BottomSheetState {
        data object Default : BottomSheetState
        data object Updating : BottomSheetState
        data object InvalidInput : BottomSheetState
        data object ApiError : BottomSheetState
    }
}

@Stable
data class Entry(
    val entryId: Int,
    val type: Type,
    val title: String,
    val posterImage: String,
    val coverImage: String,
    val progressPercent: Float,
    val progress: Int,
    val maxProgress: Int?,
    val progressDisplay: String,
    val airingTimeFrame: String,
    val entryStatus: EntryStatus,
    val seriesStatus: String,
    val isUpdating: Boolean,
    val canUpdate: Boolean
)
