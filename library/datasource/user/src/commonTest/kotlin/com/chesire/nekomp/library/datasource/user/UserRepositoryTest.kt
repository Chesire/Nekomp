package com.chesire.nekomp.library.datasource.user

import com.chesire.nekomp.library.datasource.user.local.UserStorage
import com.chesire.nekomp.library.datasource.user.remote.UserApi
import com.chesire.nekomp.library.datasource.user.remote.model.UserItemDto
import com.chesire.nekomp.library.datasource.user.remote.model.UserResponseDto
import com.github.michaelbull.result.Result
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.resetAnswers
import dev.mokkery.resetCalls
import dev.mokkery.verifySuspend
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.types.shouldBeTypeOf
import kotlin.Result as KResult

class UserRepositoryTest : FunSpec({

    val userApi = mock<UserApi>()
    val userStorage = mock<UserStorage>()
    lateinit var repository: UserRepository

    val userResponseFake = UserResponseDto(
        data = listOf(
            UserItemDto(
                id = 123,
                attributes = UserItemDto.Attributes(
                    name = "Name",
                    avatar = null,
                    coverImage = null
                )
            )
        )
    )

    beforeTest {
        resetCalls()
        resetAnswers()

        repository = UserRepository(userApi, userStorage)
    }

    test("Given retrieveUser succeeds, When retrieve, Then storage is updated") {
        everySuspend {
            userApi.retrieveUser()
        } returns KResult.success(userResponseFake)
        everySuspend { userStorage.updateUser(any()) } returns Unit

        repository.retrieve()

        verifySuspend { userStorage.updateUser(any()) }
    }

    test("Given retrieveUser succeeds, When retrieve, Then Ok is returned") {
        everySuspend {
            userApi.retrieveUser()
        } returns KResult.success(userResponseFake)
        everySuspend { userStorage.updateUser(any()) } returns Unit

        val result = repository.retrieve()

        result.shouldBeTypeOf<Result<User, Nothing>>()
    }

    test("Given retrieveUser fails, When retrieve, Then Err is returned") {
        everySuspend {
            userApi.retrieveUser()
        } returns KResult.success(userResponseFake)
        everySuspend { userStorage.updateUser(any()) } returns Unit

        val result = repository.retrieve()

        result.shouldBeTypeOf<Result<Nothing, Unit>>()
    }
})
