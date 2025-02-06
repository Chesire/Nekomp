package com.chesire.nekomp.feature.login

import com.chesire.nekomp.feature.login.ui.LoginViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val loginFeatureModule = module {
    viewModelOf(::LoginViewModel)
}
