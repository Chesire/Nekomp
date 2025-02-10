package com.chesire.nekomp.core.database

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.Module
import org.koin.dsl.module

private const val DB_NAME = "nekomp.db"

val databaseModule: Module = module {
    single<RoomDatabase> {
        roomBuilder(DB_NAME)
            .fallbackToDestructiveMigrationOnDowngrade(true)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
}

expect fun roomBuilder(dbName: String): RoomDatabase.Builder<AppDatabase>
