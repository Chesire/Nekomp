package com.chesire.nekomp.feature.library

import com.chesire.nekomp.feature.library.ui.LibraryViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureLibraryModule = module {
    viewModelOf(::LibraryViewModel)
}
