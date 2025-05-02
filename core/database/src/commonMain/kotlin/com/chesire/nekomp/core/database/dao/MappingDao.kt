package com.chesire.nekomp.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.chesire.nekomp.core.database.entity.MappingEntity

@Dao
interface MappingDao {

    @Query("DELETE FROM MappingEntity")
    suspend fun delete(): Int

    @Query("SELECT * FROM MappingEntity WHERE malId == :malId LIMIT 1")
    suspend fun entityFromMalId(malId: Int): MappingEntity?

    @Query("SELECT * FROM MappingEntity WHERE kitsuId == :kitsuId LIMIT 1")
    suspend fun entityFromKitsuId(kitsuId: Int): MappingEntity?

    @Query("SELECT * FROM MappingEntity WHERE aniListId == :aniListId LIMIT 1")
    suspend fun entityFromAniListId(aniListId: Int): MappingEntity?

    @Query("SELECT EXISTS(SELECT * FROM MappingEntity)")
    suspend fun exists(): Boolean

    @Upsert(entity = MappingEntity::class)
    suspend fun upsert(mappings: List<MappingEntity>)
}
