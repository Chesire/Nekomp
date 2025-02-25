package com.chesire.nekomp.core.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val THEME = "THEME_KEY"

class ApplicationSettings(private val preferences: DataStore<Preferences>) {

    val theme: Flow<Theme> = preferences.data.map {
        (it[stringPreferencesKey(THEME)] ?: Theme.System.name).let {
            Theme.fromString(it)
        }
    }

    suspend fun updateTheme(newTheme: Theme) {
        preferences.edit {
            it[stringPreferencesKey(THEME)] = newTheme.name
        }
    }
}

enum class Theme {
    System,
    Light,
    Dark;

    companion object {

        fun fromString(input: String): Theme {
            return Theme.entries.find { it.name.lowercase() == input.lowercase() } ?: System
        }
    }
}
