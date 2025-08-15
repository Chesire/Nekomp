package com.chesire.nekomp.feature.library.ui

import com.chesire.nekomp.core.model.EntryStatus
import com.chesire.nekomp.core.model.Type
import com.chesire.nekomp.feature.library.data.SortChoice
import com.chesire.nekomp.feature.library.data.ViewType

sealed interface ViewAction {

    data object ViewTypeClick : ViewAction
    data class ViewTypeChosen(val newType: ViewType?) : ViewAction

    data object SortClick : ViewAction
    data class SortChosen(val newSortChoice: SortChoice?) : ViewAction

    data class TypeFilterClick(val selectedType: Type) : ViewAction
    data class StatusFilterClick(val selectedStatus: EntryStatus) : ViewAction

    data class ItemSelect(val entry: Entry) : ViewAction
    data class ItemPlusOneClick(val entry: Entry) : ViewAction

    data class ProgressCardClick(val entry: Entry) : ViewAction
    data class ProgressUpdated(val entryId: Int, val newProgress: String?) : ViewAction
    data class RatingCardClick(val entry: Entry) : ViewAction
    data class StatusCardClick(val entry: Entry) : ViewAction

    data object ObservedViewEvent : ViewAction
}
