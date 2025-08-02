package com.chesire.nekomp.di

import com.chesire.nekomp.navigation.AuthEventHandler
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val navigationModule = module {
    singleOf(::AuthEventHandler)
}
