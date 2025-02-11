package com.chesire.nekomp.library.datasource.user

import com.chesire.nekomp.library.datasource.user.local.UserStorage
import com.chesire.nekomp.library.datasource.user.remote.UserApi
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val userApi: UserApi,
    private val userStorage: UserStorage
) {

    val user: Flow<User> = userStorage.user

    suspend fun retrieve(): Result<User> {
        // TODO: Add a remote data source that converts the dtos appropriately
        return userApi.retrieveUser()
            .map {
                val dto = it.data.first()
                User(dto.id, dto.attributes.name)
            }
            .onSuccess {
                userStorage.updateUser(it)
            }
    }
}
