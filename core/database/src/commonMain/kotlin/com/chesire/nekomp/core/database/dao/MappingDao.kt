package com.chesire.nekomp.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.chesire.nekomp.core.database.entity.MappingEntity

@Dao
interface MappingDao {

    @Query("DELETE FROM MappingEntity")
    suspend fun delete(): Int

    @Upsert(entity = MappingEntity::class)
    suspend fun upsert(mappings: List<MappingEntity>)

    @Query("SELECT * FROM MappingEntity WHERE malId == :malId LIMIT 1")
    suspend fun entityFromMalId(malId: Int): MappingEntity?

    @Query("SELECT * FROM MappingEntity WHERE malId == :kitsuId LIMIT 1")
    suspend fun entityFromKitsuId(kitsuId: Int): MappingEntity?
}
