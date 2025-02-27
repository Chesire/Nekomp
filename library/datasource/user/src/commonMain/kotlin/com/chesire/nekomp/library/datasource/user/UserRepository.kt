package com.chesire.nekomp.library.datasource.user

import com.chesire.nekomp.library.datasource.kitsumodels.toImage
import com.chesire.nekomp.library.datasource.user.local.UserStorage
import com.chesire.nekomp.library.datasource.user.remote.UserApi
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import kotlinx.coroutines.flow.Flow

// TODO: Add a remote data source that converts the dtos appropriately
class UserRepository(
    private val userApi: UserApi,
    private val userStorage: UserStorage
) {

    val user: Flow<User> = userStorage.user

    suspend fun retrieve(): Result<User, Unit> {
        return userApi.retrieveUser()
            .map {
                val dto = it.data.first()
                User(
                    id = dto.id,
                    name = dto.attributes.name,
                    posterImage = dto.attributes.avatar.toImage(),
                    coverImage = dto.attributes.coverImage.toImage()
                )
            }
            .onSuccess {
                userStorage.updateUser(it)
            }
            .fold(
                onSuccess = { Ok(it) },
                onFailure = { Err(Unit) }
            )
    }
}
