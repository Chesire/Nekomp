package com.chesire.nekomp.feature.discover.core

import com.chesire.nekomp.feature.discover.data.RecentSearchesStorage
import kotlinx.coroutines.flow.first

class RecentSearchesUseCase(private val storage: RecentSearchesStorage) {

    val recents = storage.recentSearches

    suspend fun addRecentSearch(text: String) {
        recents.first().toMutableList().apply {
            remove(text)
            add(text)
            if (size > 10) {
                removeAt(0)
            }
        }.let {
            storage.updateRecentSearches(it.toSet())
        }
    }
}
