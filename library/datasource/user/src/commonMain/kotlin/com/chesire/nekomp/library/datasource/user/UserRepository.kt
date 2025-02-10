package com.chesire.nekomp.library.datasource.user

import com.chesire.nekomp.library.datasource.user.local.UserStorage
import com.chesire.nekomp.library.datasource.user.remote.UserApi

class UserRepository(
    private val userApi: UserApi,
    private val userStorage: UserStorage
) {

    val user: User
        get() = userStorage.user

    suspend fun retrieveUser(): Result<User> {
        return userApi.retrieveUser()
            .map {
                val dto = it.data.first()
                User(dto.id, dto.attributes.name)
            }
            .onSuccess {
                userStorage.user = it
            }
    }
}
