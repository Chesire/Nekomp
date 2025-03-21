package com.chesire.nekomp.di

import com.chesire.nekomp.Initializers
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val initializersModule = module {
    factoryOf(::Initializers)
}
