package com.chesire.nekomp.library.datasource.user.local

import com.chesire.nekomp.core.database.dao.UserDao
import com.chesire.nekomp.core.database.entity.UserEntity
import com.chesire.nekomp.library.datasource.user.User
import kotlinx.coroutines.flow.map

class UserStorage(private val userDao: UserDao) {

    val user = userDao
        .user()
        .map {
            if (it == null) {
                User(
                    id = -1,
                    name = "",
                    isAuthenticated = false
                )
            } else {
                User(
                    id = it.id,
                    name = it.name
                )
            }
        }

    suspend fun updateUser(user: User) {
        val entity = UserEntity(
            id = user.id,
            name = user.name
        )
        userDao.upsert(entity)
    }
}
