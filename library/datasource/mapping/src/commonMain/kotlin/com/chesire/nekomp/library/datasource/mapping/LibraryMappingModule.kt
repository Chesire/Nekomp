package com.chesire.nekomp.library.datasource.mapping

import com.chesire.nekomp.library.datasource.mapping.local.MappingLocalDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val libraryMappingModule = module {
    singleOf(::MappingRepository)
    singleOf(::MappingLocalDataSource)
}
