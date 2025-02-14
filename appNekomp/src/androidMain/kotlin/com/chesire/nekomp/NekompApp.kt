package com.chesire.nekomp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class NekompApp : Application() {

    override fun onCreate() {
        super.onCreate()

        initDi()
    }

    private fun initDi() {
        initKoin {
            androidContext(this@NekompApp)
            androidLogger(Level.INFO)
        }
    }
}
