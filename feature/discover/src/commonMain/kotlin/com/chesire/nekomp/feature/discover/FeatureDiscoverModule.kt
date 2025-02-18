package com.chesire.nekomp.feature.discover

import com.chesire.nekomp.feature.discover.ui.DiscoverViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureDiscoverModule = module {
    viewModelOf(::DiscoverViewModel)
}
