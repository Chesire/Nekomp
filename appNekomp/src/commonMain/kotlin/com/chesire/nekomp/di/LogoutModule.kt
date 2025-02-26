package com.chesire.nekomp.di

import com.chesire.nekomp.binder.LogoutBinder
import com.chesire.nekomp.feature.settings.core.LogoutExecutor
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val logoutModule = module {
    factoryOf(::LogoutBinder).bind(LogoutExecutor::class)
}
