package com.chesire.nekomp.feature.discover.core

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// TODO: Store this in the preferences instead
class RecentSearchesUseCase {

    private val _recents = MutableStateFlow<List<String>>(emptyList())
    val recents = _recents.asStateFlow()

    fun addRecentSearch(text: String) {
        _recents.update {
            it.toMutableList().apply {
                remove(text)
                add(text)
                if (size > 10) {
                    removeAt(0)
                }
            }
        }
    }
}
