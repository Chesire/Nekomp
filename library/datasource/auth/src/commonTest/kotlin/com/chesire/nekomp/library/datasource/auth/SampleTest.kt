package com.chesire.nekomp.library.datasource.auth

import com.chesire.nekomp.library.datasource.auth.remote.model.LoginRequestDto
import kotlin.test.Test
import kotlin.test.assertTrue

class SampleTest {

    @Test
    fun `Sample test remove later`() {
        val a = LoginRequestDto("Username", "Password", "GrantType")
        assertTrue { a.username == "Username" }
    }
}
