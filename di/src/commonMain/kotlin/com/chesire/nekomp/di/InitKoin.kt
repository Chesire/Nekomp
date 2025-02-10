@file:OptIn(ExperimentalObjCRefinement::class)

package com.chesire.nekomp.di

import com.chesire.nekomp.feature.login.featureLoginModule
import com.chesire.nekomp.library.datasource.auth.libraryAuthModule
import com.chesire.nekomp.library.datasource.library.libraryLibraryModule
import com.chesire.nekomp.library.datasource.user.libraryUserModule
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
                featureLoginModule,
                libraryAuthModule,
                libraryLibraryModule,
                libraryUserModule
            )
        )
    }
}
