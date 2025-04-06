package com.chesire.nekomp.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.chesire.nekomp.core.database.entity.AiringEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AiringDao {

    @Upsert
    suspend fun upsert(entries: AiringEntity)

    @Upsert
    suspend fun upsert(entries: List<AiringEntity>)

    @Query("SELECT * FROM AiringEntity")
    fun entries(): Flow<List<AiringEntity>>

    @Query("SELECT * FROM AiringEntity")
    suspend fun entriesSync(): List<AiringEntity>

    @Query("DELETE FROM AiringEntity WHERE malId in (:entries)")
    suspend fun delete(entries: List<Int>): Int
}
