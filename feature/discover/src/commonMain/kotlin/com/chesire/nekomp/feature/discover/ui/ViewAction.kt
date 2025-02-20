package com.chesire.nekomp.feature.discover.ui

sealed interface ViewAction {
    data object SearchFocused : ViewAction
    data class TrackTrendingItemClick(val discoverItem: DiscoverItem) : ViewAction
    data object ObservedViewEvent : ViewAction
}
