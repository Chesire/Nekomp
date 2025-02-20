package com.chesire.nekomp.di

import android.content.Context
import androidx.work.WorkManager
import com.chesire.nekomp.workers.RefreshTrendingDataWorker
import com.chesire.nekomp.workers.WorkerQueue
import org.koin.androidx.workmanager.dsl.worker
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val workManagerModule = module {
    single<WorkManager> {
        WorkManager.getInstance(get<Context>())
    }
    singleOf(::WorkerQueue)
    worker { RefreshTrendingDataWorker(get(), get(), get()) }
}
