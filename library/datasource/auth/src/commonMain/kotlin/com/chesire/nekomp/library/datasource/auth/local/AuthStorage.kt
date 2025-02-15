package com.chesire.nekomp.library.datasource.auth.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull

private const val ACCESS_TOKEN = "ACCESS_TOKEN_KEY"
private const val REFRESH_TOKEN = "REFRESH_TOKEN_KEY"

class AuthStorage(private val preferences: DataStore<Preferences>) {

    suspend fun acquireAccessToken(): String? {
        return preferences.data.firstOrNull()?.get(stringPreferencesKey(ACCESS_TOKEN))
    }

    suspend fun updateAccessToken(newToken: String) {
        preferences.edit {
            it[stringPreferencesKey(ACCESS_TOKEN)] = newToken
        }
    }

    suspend fun acquireRefreshToken(): String? {
        return preferences.data.firstOrNull()?.get(stringPreferencesKey(REFRESH_TOKEN))
    }

    suspend fun updateRefreshToken(newToken: String) {
        preferences.edit {
            it[stringPreferencesKey(REFRESH_TOKEN)] = newToken
        }
    }

    suspend fun clear() {
        preferences.edit {
            it.clear()
        }
    }
}
