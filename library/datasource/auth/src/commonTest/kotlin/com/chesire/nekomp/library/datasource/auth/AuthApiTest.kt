package com.chesire.nekomp.library.datasource.auth

import com.chesire.nekomp.api.createAuthApi
import com.chesire.nekomp.library.datasource.auth.api.Either
import com.chesire.nekomp.library.datasource.auth.api.EitherConverterFactory
import com.chesire.nekomp.library.datasource.auth.model.LoginRequestDto
import de.jensklingenberg.ktorfit.ktorfitBuilder
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.fail
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json

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
                    install(ContentNegotiation) {
                        json(
                            Json {
                                isLenient = true
                                ignoreUnknownKeys = true
                            }
                        )
                    }
                }
            )
            converterFactories(EitherConverterFactory())
        }.build().createAuthApi()
        val result = api.login(body)

        if (result is Either.Success) {
            val data = result.data
            val token = data.accessToken
            assertTrue { token.isNotBlank() }
        } else {
            fail("API call failed - $result")
        }
    }
}
