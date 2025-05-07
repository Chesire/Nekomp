package com.chesire.nekomp.library.datasource.user

import com.chesire.nekomp.core.network.NetworkError
import com.chesire.nekomp.library.datasource.kitsumodels.toImage
import com.chesire.nekomp.library.datasource.user.local.UserStorage
import com.chesire.nekomp.library.datasource.user.remote.UserApi
import com.chesire.nekomp.library.datasource.user.remote.model.UserItemDto
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.map
import com.github.michaelbull.result.onSuccess
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val userApi: UserApi,
    private val userStorage: UserStorage
) {

    val user: Flow<User>
        get() = userStorage.user

    suspend fun retrieve(): Result<User, NetworkError> {
        return userApi.retrieveUser()
            .map { it.data.first().toUser() }
            .onSuccess { userStorage.updateUser(it) }
    }

    private fun UserItemDto.toUser(): User {
        return User(
            id = id,
            name = attributes.name,
            about = attributes.about,
            avatar = attributes.avatar.toImage(),
            coverImage = attributes.coverImage.toImage()
        )
    }
}
