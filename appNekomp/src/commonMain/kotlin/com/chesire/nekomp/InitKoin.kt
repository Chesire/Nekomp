@file:OptIn(ExperimentalObjCRefinement::class)

package com.chesire.nekomp

import com.chesire.nekomp.core.coroutines.coroutinesModule
import com.chesire.nekomp.core.database.databaseModule
import com.chesire.nekomp.core.preferences.preferencesModule
import com.chesire.nekomp.di.initializersModule
import com.chesire.nekomp.di.logoutModule
import com.chesire.nekomp.feature.airing.featureAiringModule
import com.chesire.nekomp.feature.discover.featureDiscoverModule
import com.chesire.nekomp.feature.home.featureHomeModule
import com.chesire.nekomp.feature.library.featureLibraryModule
import com.chesire.nekomp.feature.login.featureLoginModule
import com.chesire.nekomp.feature.profile.featureProfileModule
import com.chesire.nekomp.feature.settings.featureSettingsModule
import com.chesire.nekomp.library.datasource.airing.libraryAiringModule
import com.chesire.nekomp.library.datasource.auth.libraryAuthModule
import com.chesire.nekomp.library.datasource.library.libraryLibraryModule
import com.chesire.nekomp.library.datasource.search.librarySearchModule
import com.chesire.nekomp.library.datasource.trending.libraryTrendingModule
import com.chesire.nekomp.library.datasource.user.libraryUserModule
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

@HiddenFromObjC
fun initKoin(
    platformModules: List<Module> = emptyList(),
    appDeclaration: KoinAppDeclaration
) {
    startKoin {
        appDeclaration()
        modules(
            platformModules + koinModules
        )
    }
}

val koinModules = listOf(
    coroutinesModule,
    databaseModule,
    featureAiringModule,
    featureDiscoverModule,
    featureHomeModule,
    featureLibraryModule,
    featureLoginModule,
    featureProfileModule,
    featureSettingsModule,
    initializersModule,
    libraryAiringModule,
    libraryAuthModule,
    libraryLibraryModule,
    librarySearchModule,
    libraryTrendingModule,
    libraryUserModule,
    logoutModule,
    preferencesModule
)
