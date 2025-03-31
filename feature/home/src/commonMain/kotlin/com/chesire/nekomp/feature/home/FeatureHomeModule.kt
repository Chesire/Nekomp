package com.chesire.nekomp.feature.home

import com.chesire.nekomp.feature.home.core.ShowAiringSeriesUseCase
import com.chesire.nekomp.feature.home.ui.HomeViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureHomeModule = module {
    singleOf(::ShowAiringSeriesUseCase)
    viewModelOf(::HomeViewModel)
}
