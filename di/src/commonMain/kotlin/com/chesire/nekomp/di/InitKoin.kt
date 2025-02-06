@file:OptIn(ExperimentalObjCRefinement::class)

package com.chesire.nekomp.di

import com.chesire.nekomp.feature.login.loginFeatureModule
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

@HiddenFromObjC
fun initKoin(appDeclaration: KoinAppDeclaration) {
    startKoin {
        appDeclaration()
        modules(
            listOf(
                loginFeatureModule
            )
        )
    }
}
