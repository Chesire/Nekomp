package com.chesire.nekomp.library.datasource.user

import com.chesire.nekomp.library.datasource.kitsumodels.toImage
import com.chesire.nekomp.library.datasource.user.local.UserStorage
import com.chesire.nekomp.library.datasource.user.remote.UserApi
import com.chesire.nekomp.library.datasource.user.remote.model.UserItemDto
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val userApi: UserApi,
    private val userStorage: UserStorage
) {

    val user: Flow<User> get() = userStorage.user

    suspend fun retrieve(): Result<User, Unit> {
        return userApi.retrieveUser()
            .map { it.data.first().toUser() }
            .onSuccess { userStorage.updateUser(it) }
            .fold(
                onSuccess = { Ok(it) },
                onFailure = { Err(Unit) }
            )
    }

    private fun UserItemDto.toUser(): User {
        return User(
            id = id,
            name = attributes.name,
            avatar = attributes.avatar.toImage(),
            coverImage = attributes.coverImage.toImage()
        )
    }
}
