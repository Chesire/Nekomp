package com.chesire.nekomp.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.chesire.nekomp.core.database.entity.LibraryEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LibraryEntryDao {

    @Upsert
    suspend fun upsert(series: LibraryEntryEntity)

    @Upsert
    suspend fun upsert(series: List<LibraryEntryEntity>)

    @Query("SELECT * FROM LibraryEntryEntity")
    fun series(): Flow<List<LibraryEntryEntity>>

    @Query("SELECT * FROM LibraryEntryEntity WHERE userId = :userSeriesId LIMIT 1")
    suspend fun series(userSeriesId: Int): LibraryEntryEntity
}
