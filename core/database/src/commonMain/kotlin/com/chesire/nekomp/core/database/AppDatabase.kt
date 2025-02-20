package com.chesire.nekomp.core.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.chesire.nekomp.core.database.dao.LibraryEntryDao
import com.chesire.nekomp.core.database.dao.MostPopularDao
import com.chesire.nekomp.core.database.dao.TopRatedDao
import com.chesire.nekomp.core.database.dao.TrendingDao
import com.chesire.nekomp.core.database.dao.UserDao
import com.chesire.nekomp.core.database.entity.LibraryEntryEntity
import com.chesire.nekomp.core.database.entity.TrendingEntity
import com.chesire.nekomp.core.database.entity.UserEntity

@Database(
    entities = [
        LibraryEntryEntity::class,
        TrendingEntity::class,
        UserEntity::class
    ],
    version = 1
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getLibraryEntryDao(): LibraryEntryDao
    abstract fun getMostPopularDao(): MostPopularDao
    abstract fun getTopRatedDao(): TopRatedDao
    abstract fun getTrendingDao(): TrendingDao
    abstract fun getUserDao(): UserDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
