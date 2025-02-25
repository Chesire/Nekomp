@file:OptIn(ExperimentalObjCRefinement::class)

package com.chesire.nekomp

import com.chesire.nekomp.core.database.databaseModule
import com.chesire.nekomp.core.preferences.preferencesModule
import com.chesire.nekomp.feature.discover.featureDiscoverModule
import com.chesire.nekomp.feature.library.featureLibraryModule
import com.chesire.nekomp.feature.login.featureLoginModule
import com.chesire.nekomp.feature.settings.featureSettingsModule
import com.chesire.nekomp.library.datasource.auth.libraryAuthModule
import com.chesire.nekomp.library.datasource.library.libraryLibraryModule
import com.chesire.nekomp.library.datasource.search.librarySearchModule
import com.chesire.nekomp.library.datasource.trending.libraryTrendingModule
import com.chesire.nekomp.library.datasource.user.libraryUserModule
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

@HiddenFromObjC
fun initKoin(
    platformModules: List<Module> = emptyList(),
    appDeclaration: KoinAppDeclaration
) {
    startKoin {
        appDeclaration()
        modules(
            platformModules + listOf(
                databaseModule,
                featureDiscoverModule,
                featureLibraryModule,
                featureLoginModule,
                featureSettingsModule,
                libraryAuthModule,
                libraryLibraryModule,
                librarySearchModule,
                libraryTrendingModule,
                libraryUserModule,
                preferencesModule
            )
        )
    }
}
