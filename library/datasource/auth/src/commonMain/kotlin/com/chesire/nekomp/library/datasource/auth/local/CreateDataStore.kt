package com.chesire.nekomp.library.datasource.auth.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

internal const val dataStoreFileName = "auth.preferences_pb"

fun createDataStore(): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(produceFile = { producePath().toPath() })
}

expect fun producePath(): String
