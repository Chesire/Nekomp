@file:Suppress("MagicNumber")

package com.chesire.nekomp.workers

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

private const val AIRING_REFRESH_TAG = "AiringRefresh"
private const val AIRING_UNIQUE_NAME = "AiringSync"
private const val LIBRARY_REFRESH_TAG = "LibraryRefresh"
private const val LIBRARY_UNIQUE_NAME = "LibrarySync"
private const val MAPPING_REFRESH_TAG = "MappingRefresh"
private const val MAPPING_UNIQUE_NAME = "MappingSync"
private const val TRENDING_REFRESH_TAG = "TrendingRefresh"
private const val TRENDING_UNIQUE_NAME = "TrendingSync"
private const val USER_REFRESH_TAG = "UserRefresh"
private const val USER_UNIQUE_NAME = "UserSync"

class WorkerQueue(private val workManager: WorkManager) {

    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    fun enqueueAiringRefresh() {
        val request = PeriodicWorkRequestBuilder<RefreshAiringWorker>(24, TimeUnit.HOURS)
            .setConstraints(constraints)
            .addTag(AIRING_REFRESH_TAG)
            .build()

        workManager.enqueueUniquePeriodicWork(
            AIRING_UNIQUE_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    fun enqueueLibraryRefresh() {
        val request = PeriodicWorkRequestBuilder<RefreshLibraryWorker>(12, TimeUnit.HOURS)
            .setConstraints(constraints)
            .addTag(LIBRARY_REFRESH_TAG)
            .build()

        workManager.enqueueUniquePeriodicWork(
            LIBRARY_UNIQUE_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    fun enqueueMappingRefresh() {
        val request = PeriodicWorkRequestBuilder<RefreshMappingWorker>(12, TimeUnit.HOURS)
            .setConstraints(constraints)
            .addTag(MAPPING_REFRESH_TAG)
            .build()

        workManager.enqueueUniquePeriodicWork(
            MAPPING_UNIQUE_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

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

    fun enqueueUserRefresh() {
        val request = PeriodicWorkRequestBuilder<RefreshUserWorker>(12, TimeUnit.HOURS)
            .setConstraints(constraints)
            .addTag(USER_REFRESH_TAG)
            .build()

        workManager.enqueueUniquePeriodicWork(
            USER_UNIQUE_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }
}
