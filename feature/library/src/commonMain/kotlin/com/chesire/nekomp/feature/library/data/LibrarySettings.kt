package com.chesire.nekomp.feature.library.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val VIEW_TYPE = "VIEW_TYPE"

class LibrarySettings(private val preferences: DataStore<Preferences>) {

    val viewType: Flow<ViewType> = preferences.data.map {
        (it[stringPreferencesKey(VIEW_TYPE)] ?: ViewType.default.name).let {
            ViewType.fromString(it)
        }
    }

    suspend fun updateViewType(newViewType: ViewType) {
        preferences.edit {
            it[stringPreferencesKey(VIEW_TYPE)] = newViewType.name
        }
    }
}
