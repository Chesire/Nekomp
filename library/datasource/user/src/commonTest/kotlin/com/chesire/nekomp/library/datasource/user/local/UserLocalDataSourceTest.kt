package com.chesire.nekomp.library.datasource.user.local

import com.chesire.nekomp.core.database.dao.UserDao
import com.chesire.nekomp.core.model.Image
import com.chesire.nekomp.library.datasource.user.User
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.resetAnswers
import dev.mokkery.resetCalls
import dev.mokkery.verifySuspend
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class UserLocalDataSourceTest : FunSpec({

    val userDao = mock<UserDao>()
    lateinit var userStorage: UserStorage

    beforeTest {
        resetCalls()
        resetAnswers()

        userStorage = UserLocalDataSource(userDao)
    }

    test("Given null user, When calling user, Then flow of empty user is returned") {
        everySuspend { userDao.user() } returns flowOf(null)

        val user = userStorage.user.first()

        user.apply {
            id.shouldBe(-1)
            name.shouldBe("")
            avatar.shouldBe(Image.empty)
            coverImage.shouldBe(Image.empty)
            isAuthenticated.shouldBeFalse()
        }
    }

    test("When updateUser, Then dao is updated") {
        everySuspend { userDao.upsert(any()) } returns Unit
        val userFake = User(
            id = 2784,
            name = "",
            avatar = Image(
                tiny = "c",
                small = "",
                medium = "",
                large = "",
                original = ""
            ),
            coverImage = Image(
                tiny = "",
                small = "",
                medium = "",
                large = "",
                original = ""
            ),
            isAuthenticated = false
        )
        userStorage.updateUser(userFake)

        verifySuspend { userDao.upsert(any()) }
    }
})
