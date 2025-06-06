package com.chesire.nekomp.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import co.touchlab.kermit.Logger
import com.chesire.nekomp.library.datasource.auth.AuthRepository
import com.chesire.nekomp.library.datasource.trending.TrendingRepository
import com.github.michaelbull.result.anyErr

private const val MAX_RETRIES = 1

class RefreshTrendingDataWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val trendingRepository: TrendingRepository,
    private val authRepository: AuthRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Logger.d("RefreshingTrendingDataWorker") { "Starting worker for refreshing trending" }

        return if (authRepository.accessToken().isNullOrBlank()) {
            Logger.d("RefreshLibraryWorker") { "No user stored, exiting early" }
            Result.success()
        } else {
            val result = trendingRepository.performFullSync()
            return if (result.anyErr()) {
                if (runAttemptCount < MAX_RETRIES) {
                    Logger.d("RefreshingTrendingDataWorker") { "Worker failure, retrying" }
                    Result.retry()
                } else {
                    Logger.d("RefreshingTrendingDataWorker") { "Worker failure, not retrying" }
                    Result.failure()
                }
            } else {
                Logger.d("RefreshingTrendingDataWorker") { "Worker successful" }
                Result.success()
            }
        }
    }
}
