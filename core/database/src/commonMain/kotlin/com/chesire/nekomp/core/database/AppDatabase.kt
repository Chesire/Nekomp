package com.chesire.nekomp.core.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.chesire.nekomp.core.database.dao.AiringDao
import com.chesire.nekomp.core.database.dao.LibraryEntryDao
import com.chesire.nekomp.core.database.dao.TrendingDao
import com.chesire.nekomp.core.database.dao.UserDao
import com.chesire.nekomp.core.database.entity.AiringEntity
import com.chesire.nekomp.core.database.entity.LibraryEntryEntity
import com.chesire.nekomp.core.database.entity.TrendingEntity
import com.chesire.nekomp.core.database.entity.UserEntity

@Database(
    entities = [
        AiringEntity::class,
        LibraryEntryEntity::class,
        TrendingEntity::class,
        UserEntity::class
    ],
    version = 1
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getAiringDao(): AiringDao
    abstract fun getLibraryEntryDao(): LibraryEntryDao
    abstract fun getTrendingDao(): TrendingDao
    abstract fun getUserDao(): UserDao
}

// The Room compiler generates the `actual` implementations.
@Suppress(
    "KotlinNoActualForExpect",
    "NO_ACTUAL_FOR_EXPECT",
    "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING"
)
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
