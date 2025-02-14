@file:OptIn(ExperimentalObjCRefinement::class)

package com.chesire.nekomp

import com.chesire.nekomp.core.database.databaseModule
import com.chesire.nekomp.feature.library.featureLibraryModule
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
                databaseModule,
                featureLibraryModule,
                featureLoginModule,
                libraryAuthModule,
                libraryLibraryModule,
                libraryUserModule
            )
        )
    }
}
