package com.chesire.nekomp.feature.discover.ui

sealed interface ViewAction {
    data class SearchTextUpdated(val newSearchText: String) : ViewAction
    data object SearchExecute : ViewAction
    data class RecentSearchClick(val recentSearchTerm: String) : ViewAction
    data class ItemSelect(val discoverItem: DiscoverItem) : ViewAction
    data class TrackItemClick(val discoverItem: DiscoverItem) : ViewAction
    data object ObservedViewEvent : ViewAction
}
