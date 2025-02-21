package com.chesire.nekomp.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import co.touchlab.kermit.Logger
import com.chesire.nekomp.library.datasource.auth.AuthRepository
import com.chesire.nekomp.library.datasource.user.UserRepository
import com.github.michaelbull.result.mapBoth

private const val MAX_RETRIES = 1

class RefreshUserWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Logger.d("RefreshUserWorker") { "Starting worker for refreshing user" }
        return if (authRepository.accessToken().isNullOrBlank()) {
            Logger.d("RefreshUserWorker") { "No user stored, exiting early" }
            Result.success()
        } else {
            userRepository.retrieve()
                .mapBoth(
                    success = {
                        Logger.d("RefreshUserWorker") { "Worker successful" }
                        Result.success()
                    },
                    failure = {
                        if (runAttemptCount < MAX_RETRIES) {
                            Logger.d("RefreshUserWorker") { "Worker failure, retrying" }
                            Result.retry()
                        } else {
                            Logger.d("RefreshUserWorker") { "Worker failure, not retrying" }
                            Result.failure()
                        }
                    }
                )
        }
    }
}
