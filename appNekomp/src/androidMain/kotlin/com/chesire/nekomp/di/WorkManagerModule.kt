package com.chesire.nekomp.di

import android.content.Context
import androidx.work.WorkManager
import com.chesire.nekomp.workers.RefreshAiringWorker
import com.chesire.nekomp.workers.RefreshLibraryWorker
import com.chesire.nekomp.workers.RefreshTrendingDataWorker
import com.chesire.nekomp.workers.RefreshUserWorker
import com.chesire.nekomp.workers.WorkerQueue
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val workManagerModule = module {
    single<WorkManager> {
        WorkManager.getInstance(get<Context>())
    }
    singleOf(::WorkerQueue)
    workerOf(::RefreshAiringWorker)
    workerOf(::RefreshLibraryWorker)
    workerOf(::RefreshTrendingDataWorker)
    workerOf(::RefreshUserWorker)
}
