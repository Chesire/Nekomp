package com.chesire.nekomp.library.datasource.user.local

import com.chesire.nekomp.library.datasource.user.User
import kotlinx.coroutines.flow.Flow

interface UserStorage {

    val user: Flow<User>
    suspend fun updateUser(user: User)
}
