package com.chesire.nekomp.feature.home.ui

sealed interface ViewAction {
    data class WatchItemClick(val watchItem: WatchItem) : ViewAction
    data class WatchItemPlusOneClick(val watchItem: WatchItem) : ViewAction
    data class TrendItemClick(val trendItem: TrendItem) : ViewAction
    data object ObservedViewEvent : ViewAction
}
