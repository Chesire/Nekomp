package com.chesire.nekomp.binder

import co.touchlab.kermit.Logger
import com.chesire.nekomp.core.database.AppDatabase
import com.chesire.nekomp.core.network.RefreshErrorExecutor
import com.chesire.nekomp.feature.settings.core.LogoutExecutor
import com.chesire.nekomp.library.datasource.auth.AuthRepository
import com.chesire.nekomp.navigation.AuthEventHandler

class LogoutBinder(
    private val database: AppDatabase,
    private val authRepository: AuthRepository,
    private val authEventHandler: AuthEventHandler
) : LogoutExecutor, RefreshErrorExecutor {

    override suspend fun invoke() {
        clearAuth()
        clearDBs()
        userLoggedOut()
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

    private suspend fun userLoggedOut() {
        // Emit navigation event to redirect to login
        authEventHandler.emitUserLoggedOut()
    }
}
