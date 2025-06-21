package com.chesire.nekomp.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import co.touchlab.kermit.Logger
import com.chesire.nekomp.library.datasource.mapping.MappingRepository
import com.github.michaelbull.result.mapBoth

private const val MAX_RETRIES = 3

class RefreshMappingWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val mappingRepository: MappingRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Logger.d("RefreshMappingWorker") { "Starting worker for refreshing library" }

        return mappingRepository.updateMappings()
            .mapBoth(
                success = {
                    Logger.d("RefreshMappingWorker") { "Worker successful" }
                    Result.success()
                },
                failure = {
                    if (runAttemptCount < MAX_RETRIES) {
                        Logger.d("RefreshMappingWorker") { "Worker failure, retrying" }
                        Result.retry()
                    } else {
                        Logger.d("RefreshMappingWorker") { "Worker failure, not retrying" }
                        Result.failure()
                    }
                }
            )
    }
}
