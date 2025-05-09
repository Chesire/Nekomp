package com.chesire.nekomp.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.chesire.nekomp.core.database.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Upsert
    suspend fun upsert(entries: List<FavoriteEntity>)

    @Query("SELECT * FROM FavoriteEntity")
    fun entries(): Flow<List<FavoriteEntity>>
}
