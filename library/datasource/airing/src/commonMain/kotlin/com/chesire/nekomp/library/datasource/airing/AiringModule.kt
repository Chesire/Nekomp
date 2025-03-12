package com.chesire.nekomp.library.datasource.airing

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val libraryAiringModule = module {
    singleOf(::AiringRepository)
}
