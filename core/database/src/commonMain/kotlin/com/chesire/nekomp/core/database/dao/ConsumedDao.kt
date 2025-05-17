package com.chesire.nekomp.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.chesire.nekomp.core.database.entity.ConsumedDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConsumedDao {

    @Query("DELETE FROM ConsumedDataEntity WHERE type == :type")
    suspend fun delete(type: String): Int

    @Upsert
    suspend fun upsert(entries: ConsumedDataEntity)

    @Query("SELECT * FROM ConsumedDataEntity WHERE type == :type")
    fun entries(type: String): Flow<ConsumedDataEntity>
}
