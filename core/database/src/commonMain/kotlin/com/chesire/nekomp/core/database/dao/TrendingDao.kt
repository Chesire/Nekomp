package com.chesire.nekomp.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import co.touchlab.kermit.Logger
import com.chesire.nekomp.core.database.entity.TrendingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrendingDao {

    @Query("DELETE FROM TrendingEntity")
    suspend fun delete(): Int

    @Upsert(entity = TrendingEntity::class)
    suspend fun upsert(trending: List<TrendingEntity>)

    @Transaction
    suspend fun update(trending: List<TrendingEntity>) {
        Logger.d("TrendingDao") { "Executing call to clear trending dao" }
        val clearAmount = delete()
        Logger.d("TrendingDao") { "Finished delete call, cleared $clearAmount entries" }

        Logger.d("TrendingDao") { "Storing ${trending.count()} trending items" }
        upsert(trending)
        Logger.d("TrendingDao") { "Finished storing trending items" }
    }

    @Query("SELECT * FROM TrendingEntity WHERE type == :type")
    fun trending(type: String): Flow<List<TrendingEntity>>
}
