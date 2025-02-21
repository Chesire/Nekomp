package com.chesire.nekomp.feature.discover

import com.chesire.nekomp.feature.discover.core.AddItemToTrackingUseCase
import com.chesire.nekomp.feature.discover.core.RecentSearchesUseCase
import com.chesire.nekomp.feature.discover.core.RetrieveLibraryUseCase
import com.chesire.nekomp.feature.discover.core.RetrieveTrendingDataUseCase
import com.chesire.nekomp.feature.discover.ui.DiscoverViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureDiscoverModule = module {
    singleOf(::AddItemToTrackingUseCase)
    singleOf(::RecentSearchesUseCase)
    singleOf(::RetrieveLibraryUseCase)
    singleOf(::RetrieveTrendingDataUseCase)
    viewModelOf(::DiscoverViewModel)
}
