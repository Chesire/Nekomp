package com.chesire.nekomp.feature.login

import com.chesire.nekomp.feature.login.core.PerformLoginUseCase
import com.chesire.nekomp.feature.login.ui.LoginViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureLoginModule = module {
    factoryOf(::PerformLoginUseCase)
    viewModelOf(::LoginViewModel)
}
