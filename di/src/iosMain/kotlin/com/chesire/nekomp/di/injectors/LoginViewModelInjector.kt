package com.chesire.nekomp.di.injectors

import com.chesire.nekomp.feature.login.ui.LoginViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LoginViewModelInjector : KoinComponent {
    val loginViewModel by inject<LoginViewModel>()
}
