package com.chesire.nekomp

import android.app.Application
import com.chesire.nekomp.di.workManagerModule
import com.chesire.nekomp.workers.WorkerQueue
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.logger.Level

class NekompApp : Application() {

    override fun onCreate() {
        super.onCreate()

        initDi()
        startWorkers()
    }

    private fun initDi() {
        initKoin(
            platformModules = listOf(workManagerModule)
        ) {
            androidContext(this@NekompApp)
            androidLogger(Level.INFO)
            workManagerFactory()
        }
    }

    private fun startWorkers() {
        val workerQueue = get<WorkerQueue>()
        workerQueue.enqueueTrendingRefresh()
    }
}
