package com.chesire.nekomp.library.datasource.airing.remote

import com.chesire.nekomp.core.network.ResultConverterFactory
import com.chesire.nekomp.core.network.plugin.installContentNegotiation
import com.github.michaelbull.result.get
import de.jensklingenberg.ktorfit.ktorfitBuilder
import io.ktor.client.HttpClient
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.fail
import kotlinx.coroutines.test.runTest

class AiringApiTest {

    @Test
    @Ignore
    fun `Calling retrieveSeasonNow endpoint returns response dto`() = runTest {
        val api = ktorfitBuilder {
            baseUrl("https://api.jikan.moe/")
            httpClient(
                client = HttpClient {
                    installContentNegotiation()
                }
            )
            converterFactories(ResultConverterFactory())
        }.build().createAiringApi()

        val result = api.retrieveSeasonNow(1)
        if (result.isOk) {
            val data = result.get()
            assertTrue { data!!.data.isNotEmpty() }
        } else {
            fail("API call failed - $result")
        }
    }
}
