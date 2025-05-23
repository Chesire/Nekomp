package com.chesire.nekomp.library.datasource.auth

import com.chesire.nekomp.core.network.ResultConverterFactory
import com.chesire.nekomp.core.network.plugin.installContentNegotiation
import com.chesire.nekomp.library.datasource.auth.remote.createAuthApi
import com.chesire.nekomp.library.datasource.auth.remote.model.LoginRequestDto
import com.github.michaelbull.result.get
import de.jensklingenberg.ktorfit.ktorfitBuilder
import io.ktor.client.HttpClient
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.fail
import kotlinx.coroutines.test.runTest

class AuthApiTest {

    @Ignore
    @Test
    fun `Calling login endpoint returns auth token`() = runTest {
        val username = "" // Put an email for Kitsu here DO NOT COMMIT
        val password = "" // Put password for account here DO NOT COMMIT
        val body = LoginRequestDto(username, password, "password")

        val api = ktorfitBuilder {
            baseUrl("https://kitsu.io/")
            httpClient(
                client = HttpClient {
                    installContentNegotiation()
                }
            )
            converterFactories(ResultConverterFactory())
        }.build().createAuthApi()

        val result = api.login(body)
        if (result.isOk) {
            val data = result.get()
            val token = data!!.accessToken
            assertTrue { token.isNotBlank() }
        } else {
            fail("API call failed - $result")
        }
    }
}
