package com.chesire.nekomp.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.chesire.nekomp.core.database.entity.TrendingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrendingDao {

    @Query("DELETE FROM TrendingEntity WHERE type == :type")
    suspend fun clearType(type: String)

    @Upsert
    suspend fun upsert(trending: List<TrendingEntity>)

    @Query("SELECT * FROM TrendingEntity")
    fun trending(): Flow<List<TrendingEntity>>
}
