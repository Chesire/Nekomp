package com.chesire.nekomp.feature.settings

import com.chesire.nekomp.feature.settings.ui.SettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureSettingsModule = module {
    viewModelOf(::SettingsViewModel)
}
