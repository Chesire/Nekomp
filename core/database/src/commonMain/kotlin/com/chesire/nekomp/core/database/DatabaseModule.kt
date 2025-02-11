package com.chesire.nekomp.core.database

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.chesire.nekomp.core.database.dao.LibraryEntryDao
import com.chesire.nekomp.core.database.dao.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.dsl.module

private const val DB_NAME = "nekomp.db"

val databaseModule = module {
    single<AppDatabase> {
        roomBuilder(DB_NAME)
            .fallbackToDestructiveMigrationOnDowngrade(true)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
    single<LibraryEntryDao> {
        get<AppDatabase>().getLibraryEntryDao()
    }
    single<UserDao> {
        get<AppDatabase>().getUserDao()
    }
}

expect fun roomBuilder(dbName: String): RoomDatabase.Builder<AppDatabase>
