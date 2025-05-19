package com.chesire.nekomp.core.database

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.chesire.nekomp.core.database.dao.AiringDao
import com.chesire.nekomp.core.database.dao.ConsumedDao
import com.chesire.nekomp.core.database.dao.FavoriteDao
import com.chesire.nekomp.core.database.dao.LibraryEntryDao
import com.chesire.nekomp.core.database.dao.MappingDao
import com.chesire.nekomp.core.database.dao.TrendingDao
import com.chesire.nekomp.core.database.dao.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.dsl.module

private const val DB_NAME = "nekomp.db"

val databaseModule = module {
    single<AppDatabase> {
        roomBuilder(DB_NAME)
            .fallbackToDestructiveMigration(true)
            .fallbackToDestructiveMigrationOnDowngrade(true)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
    single<AiringDao> { get<AppDatabase>().getAiringDao() }
    single<ConsumedDao> { get<AppDatabase>().getConsumedDao() }
    single<FavoriteDao> { get<AppDatabase>().getFavoriteDao() }
    single<LibraryEntryDao> { get<AppDatabase>().getLibraryEntryDao() }
    single<MappingDao> { get<AppDatabase>().getMappingDao() }
    single<TrendingDao> { get<AppDatabase>().getTrendingDao() }
    single<UserDao> { get<AppDatabase>().getUserDao() }
}

expect fun roomBuilder(dbName: String): RoomDatabase.Builder<AppDatabase>
