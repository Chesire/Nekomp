package com.chesire.nekomp.feature.library.ui

import com.chesire.nekomp.feature.library.data.ViewType

sealed interface ViewAction {

    data object ViewTypeClick : ViewAction
    data class ViewTypeChosen(val newType: ViewType?) : ViewAction

    data class ItemPlusOneClick(val entry: Entry) : ViewAction

    data object ObservedViewEvent : ViewAction
}
