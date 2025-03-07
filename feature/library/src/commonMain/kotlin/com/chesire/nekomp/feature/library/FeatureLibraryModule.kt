package com.chesire.nekomp.feature.library

import com.chesire.nekomp.core.preferences.createDataStore
import com.chesire.nekomp.feature.library.core.ObserveLibraryEntriesUseCase
import com.chesire.nekomp.feature.library.core.RefreshLibraryEntriesUseCase
import com.chesire.nekomp.feature.library.data.LibrarySettings
import com.chesire.nekomp.feature.library.ui.LibraryViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

private const val LIBRARY_SETTINGS_DATASTORE_NAME = "nekomp.librarysettings.preferences_pb"

val featureLibraryModule = module {
    factoryOf(::ObserveLibraryEntriesUseCase)
    factoryOf(::RefreshLibraryEntriesUseCase)

    single<LibrarySettings> {
        LibrarySettings(createDataStore(LIBRARY_SETTINGS_DATASTORE_NAME))
    }

    viewModelOf(::LibraryViewModel)
}
