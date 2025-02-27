package com.chesire.nekomp.library.datasource.user.local

import com.chesire.nekomp.core.database.dao.UserDao
import com.chesire.nekomp.core.database.entity.UserEntity
import com.chesire.nekomp.core.model.Image
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
                    avatar = Image.empty,
                    coverImage = Image.empty,
                    isAuthenticated = false
                )
            } else {
                User(
                    id = it.id,
                    name = it.name,
                    avatar = Image(
                        tiny = it.avatarTiny,
                        small = it.avatarSmall,
                        medium = it.avatarMedium,
                        large = it.avatarLarge,
                        original = it.avatarOriginal
                    ),
                    coverImage = Image(
                        tiny = it.coverImageTiny,
                        small = it.coverImageSmall,
                        medium = it.coverImageMedium,
                        large = it.coverImageLarge,
                        original = it.coverImageOriginal
                    ),
                    isAuthenticated = true
                )
            }
        }

    suspend fun updateUser(user: User) {
        val entity = UserEntity(
            id = user.id,
            name = user.name,
            avatarTiny = user.avatar.tiny,
            avatarSmall = user.avatar.small,
            avatarMedium = user.avatar.medium,
            avatarLarge = user.avatar.large,
            avatarOriginal = user.avatar.original,
            coverImageTiny = user.coverImage.tiny,
            coverImageSmall = user.coverImage.small,
            coverImageMedium = user.coverImage.medium,
            coverImageLarge = user.coverImage.large,
            coverImageOriginal = user.coverImage.original
        )
        userDao.upsert(entity)
    }
}
