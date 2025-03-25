package com.chesire.nekomp

import android.app.Application
import co.touchlab.kermit.Logger
import com.chesire.nekomp.di.workManagerModule
import com.chesire.nekomp.workers.WorkerQueue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.logger.Level

class NekompApp : Application() {

    override fun onCreate() {
        super.onCreate()

        initDi()
        callInitializers()
    }

    private fun initDi() {
        initKoin(platformModules = listOf(workManagerModule)) {
            androidContext(this@NekompApp)
            androidLogger(Level.INFO)
            workManagerFactory()
        }
    }

    private fun callInitializers() {
        get<Initializers>().apply {
            MainScope().launch(Dispatchers.IO) {
                prepopulateDb()
            }.invokeOnCompletion {
                // Start workers after so we have the pre-populated DB straight away
                startWorkers()
            }
        }
    }

    private fun startWorkers() {
        Logger.d("NekompApp") { "Starting the background workers" }
        get<WorkerQueue>().apply {
            enqueueAiringRefresh()
            enqueueLibraryRefresh()
            enqueueTrendingRefresh()
            enqueueUserRefresh()
        }
    }
}
