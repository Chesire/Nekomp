package com.chesire.nekomp.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import co.touchlab.kermit.Logger
import com.chesire.nekomp.library.datasource.airing.AiringRepository
import com.github.michaelbull.result.mapBoth

private const val MAX_RETRIES = 3

class RefreshAiringWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val airingRepository: AiringRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Logger.d("RefreshAiringWorker") { "Starting worker for refreshing airing" }

        return airingRepository.syncCurrentAiring()
            .mapBoth(
                success = {
                    Logger.d("RefreshAiringWorker") { "Worker successful" }
                    Result.success()
                },
                failure = {
                    if (runAttemptCount < MAX_RETRIES) {
                        Logger.d("RefreshAiringWorker") { "Worker failure, retrying" }
                        Result.retry()
                    } else {
                        Logger.d("RefreshAiringWorker") { "Worker failure, not retrying" }
                        Result.failure()
                    }
                }
            )
    }
}
