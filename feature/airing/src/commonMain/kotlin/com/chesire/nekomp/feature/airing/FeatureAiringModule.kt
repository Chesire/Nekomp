package com.chesire.nekomp.feature.airing

import com.chesire.nekomp.feature.airing.ui.AiringViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureAiringModule = module {
    viewModelOf(::AiringViewModel)
}
