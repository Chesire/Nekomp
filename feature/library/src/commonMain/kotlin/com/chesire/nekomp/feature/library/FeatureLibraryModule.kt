package com.chesire.nekomp.feature.library

import com.chesire.nekomp.feature.library.core.ObserveLibraryEntriesUseCase
import com.chesire.nekomp.feature.library.core.RefreshLibraryEntriesUseCase
import com.chesire.nekomp.feature.library.ui.LibraryViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureLibraryModule = module {
    factoryOf(::ObserveLibraryEntriesUseCase)
    factoryOf(::RefreshLibraryEntriesUseCase)
    viewModelOf(::LibraryViewModel)
}
