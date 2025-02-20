package com.chesire.nekomp.workers

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

private const val TRENDING_REFRESH_TAG = "TrendingRefresh"
private const val TRENDING_UNIQUE_NAME = "TrendingSync"

class WorkerQueue(private val workManager: WorkManager) {

    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    fun enqueueTrendingRefresh() {
        val request = PeriodicWorkRequestBuilder<RefreshTrendingDataWorker>(12, TimeUnit.HOURS)
            .setConstraints(constraints)
            .addTag(TRENDING_REFRESH_TAG)
            .build()

        workManager.enqueueUniquePeriodicWork(
            TRENDING_UNIQUE_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }
}
