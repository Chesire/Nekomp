package com.chesire.nekomp.core.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.java.KoinJavaComponent.get

actual fun roomBuilder(dbName: String): RoomDatabase.Builder<AppDatabase> {
    val appContext = get<Context>(Context::class.java).applicationContext
    val dbFile = appContext.getDatabasePath(dbName)
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}
