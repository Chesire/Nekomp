package com.chesire.nekomp.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.chesire.nekomp.core.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Upsert
    suspend fun upsert(user: UserEntity)

    @Query("SELECT * FROM UserEntity")
    fun user(): Flow<UserEntity?>
}
