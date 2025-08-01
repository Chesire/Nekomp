package com.chesire.nekomp.binder

import co.touchlab.kermit.Logger
import com.chesire.nekomp.core.database.AppDatabase
import com.chesire.nekomp.core.network.RefreshErrorExecutor
import com.chesire.nekomp.feature.settings.core.LogoutExecutor
import com.chesire.nekomp.library.datasource.auth.AuthRepository

class LogoutBinder(
    private val database: AppDatabase,
    private val authRepository: AuthRepository
) : LogoutExecutor, RefreshErrorExecutor {

    override suspend fun invoke() {
        clearAuth()
        clearDBs()
    }

    private suspend fun clearAuth() {
        Logger.d("Logout") { "Clearing auth" }
        authRepository.clear()
    }

    private suspend fun clearDBs() {
        Logger.d("Logout") { "Clearing user" }
        database.getUserDao().delete()
        Logger.d("Logout") { "Clearing library entries" }
        database.getLibraryEntryDao().delete()
    }
}
