package com.chesire.nekomp.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.chesire.nekomp.core.database.entity.LibraryEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LibraryEntryDao {

    @Upsert
    suspend fun upsert(entries: LibraryEntryEntity)

    @Upsert
    suspend fun upsert(entries: List<LibraryEntryEntity>)

    @Query("SELECT * FROM LibraryEntryEntity")
    fun entries(): Flow<List<LibraryEntryEntity>>

    @Query("DELETE FROM LibraryEntryEntity")
    suspend fun delete()
}
