package com.chesire.nekomp

import android.app.Application
import com.chesire.nekomp.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import timber.log.Timber

class NekompApp : Application() {

    override fun onCreate() {
        super.onCreate()

        initLogging()
        initDi()
    }

    private fun initLogging() {
        Timber.plant(Timber.DebugTree())
    }

    private fun initDi() {
        initKoin {
            androidContext(this@NekompApp)
            androidLogger(Level.INFO)
        }
    }
}
