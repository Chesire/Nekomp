package com.chesire.nekomp.feature.profile

import com.chesire.nekomp.feature.profile.ui.ProfileViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureProfileModule = module {
    viewModelOf(::ProfileViewModel)
}
