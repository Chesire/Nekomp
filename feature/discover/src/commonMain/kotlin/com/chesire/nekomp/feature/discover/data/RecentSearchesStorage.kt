package com.chesire.nekomp.feature.discover.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val RECENT_SEARCHES = "RECENT_SEARCHES_KEY"

class RecentSearchesStorage(private val preferences: DataStore<Preferences>) {

    val recentSearches: Flow<Set<String>> = preferences.data.map {
        it[stringSetPreferencesKey(RECENT_SEARCHES)] ?: emptySet()
    }

    suspend fun updateRecentSearches(newRecentSearches: Set<String>) {
        preferences.edit {
            it[stringSetPreferencesKey(RECENT_SEARCHES)] = newRecentSearches
        }
    }
}
