package com.chesire.nekomp.core.database

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

actual fun roomBuilder(dbName: String): RoomDatabase.Builder<AppDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), dbName)
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile.absolutePath,
    )
}
