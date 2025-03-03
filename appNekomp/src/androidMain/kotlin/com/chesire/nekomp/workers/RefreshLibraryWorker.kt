package com.chesire.nekomp.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import co.touchlab.kermit.Logger
import com.chesire.nekomp.library.datasource.auth.AuthRepository
import com.chesire.nekomp.library.datasource.library.LibraryRepository
import com.github.michaelbull.result.mapBoth

private const val MAX_RETRIES = 3

class RefreshLibraryWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val libraryRepository: LibraryRepository,
    private val authRepository: AuthRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Logger.d("RefreshLibraryWorker") { "Starting worker for refreshing library" }

        return if (authRepository.accessToken().isNullOrBlank()) {
            Logger.d("RefreshLibraryWorker") { "No user stored, exiting early" }
            Result.success()
        } else {
            libraryRepository.retrieve()
                .mapBoth(
                    success = {
                        Logger.d("RefreshLibraryWorker") { "Worker successful" }
                        Result.success()
                    },
                    failure = {
                        if (runAttemptCount < MAX_RETRIES) {
                            Logger.d("RefreshLibraryWorker") { "Worker failure, retrying" }
                            Result.retry()
                        } else {
                            Logger.d("RefreshLibraryWorker") { "Worker failure, not retrying" }
                            Result.failure()
                        }
                    }
                )
        }
    }
}
