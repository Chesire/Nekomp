package com.chesire.nekomp.library.datasource.auth

import com.chesire.nekomp.library.datasource.auth.remote.model.LoginRequestDto
import io.kotest.core.spec.style.FunSpec
import kotlin.test.assertTrue

class SampleTest : FunSpec({

    test("Sample test remove later") {
        val a = LoginRequestDto("Username", "Password", "GrantType")
        assertTrue { a.username == "Username" }
    }
})
