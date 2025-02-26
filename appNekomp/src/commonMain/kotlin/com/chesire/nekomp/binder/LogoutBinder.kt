package com.chesire.nekomp.binder

import co.touchlab.kermit.Logger
import com.chesire.nekomp.core.database.AppDatabase
import com.chesire.nekomp.feature.settings.core.LogoutExecutor
import com.chesire.nekomp.library.datasource.auth.local.AuthStorage

class LogoutBinder(
    private val database: AppDatabase,
    private val authStorage: AuthStorage
) : LogoutExecutor {

    override suspend fun execute() {
        clearAuth()
        clearDBs()
    }

    private suspend fun clearAuth() {
        Logger.Companion.d("Logout") { "Clearing auth" }
        authStorage.clear()
    }

    private suspend fun clearDBs() {
        Logger.Companion.d("Logout") { "Clearing user" }
        database.getUserDao().delete()
        Logger.Companion.d("Logout") { "Clearing library entries" }
        database.getLibraryEntryDao().delete()
    }
}
