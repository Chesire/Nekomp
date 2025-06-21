package com.chesire.nekomp.di

import com.chesire.nekomp.binder.ApplicationVersionInfoBinder
import com.chesire.nekomp.binder.LogoutBinder
import com.chesire.nekomp.feature.settings.core.LogoutExecutor
import com.chesire.nekomp.feature.settings.data.ApplicationVersionInfo
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val settingsBinderModule = module {
    factoryOf(::ApplicationVersionInfoBinder).bind(ApplicationVersionInfo::class)
    factoryOf(::LogoutBinder).bind(LogoutExecutor::class)
}
