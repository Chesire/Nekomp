package com.chesire.nekomp.feature.discover

import com.chesire.nekomp.core.preferences.createDataStore
import com.chesire.nekomp.feature.discover.core.AddItemToTrackingUseCase
import com.chesire.nekomp.feature.discover.core.RecentSearchesUseCase
import com.chesire.nekomp.feature.discover.core.RetrieveLibraryUseCase
import com.chesire.nekomp.feature.discover.core.RetrieveTrendingDataUseCase
import com.chesire.nekomp.feature.discover.core.SearchForUseCase
import com.chesire.nekomp.feature.discover.data.RecentSearchesStorage
import com.chesire.nekomp.feature.discover.ui.DiscoverViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

private const val RECENT_SEARCHES_DATASTORE_NAME = "nekomp.recentsearches.preferences_pb"

val featureDiscoverModule = module {
    factoryOf(::AddItemToTrackingUseCase)
    factoryOf(::SearchForUseCase)
    singleOf(::RecentSearchesUseCase)
    singleOf(::RetrieveLibraryUseCase)
    singleOf(::RetrieveTrendingDataUseCase)

    single<RecentSearchesStorage> {
        RecentSearchesStorage(createDataStore(RECENT_SEARCHES_DATASTORE_NAME))
    }

    viewModelOf(::DiscoverViewModel)
}
