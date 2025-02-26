package com.chesire.nekomp.core.preferences

import org.koin.dsl.module

private const val APPLICATION_SETTINGS_DATASTORE_NAME = "nekomp.applicationsettings.preferences_pb"

val preferencesModule = module {
    single<ApplicationSettings> {
        ApplicationSettings(createDataStore(APPLICATION_SETTINGS_DATASTORE_NAME))
    }
}
