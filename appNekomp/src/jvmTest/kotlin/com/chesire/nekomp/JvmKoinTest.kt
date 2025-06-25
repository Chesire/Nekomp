@file:OptIn(KoinExperimentalAPI::class)

package com.chesire.nekomp

import androidx.datastore.core.DataStore
import com.chesire.nekomp.library.datasource.mapping.remote.MappingRemoteDataSource
import io.kotest.core.spec.style.FunSpec
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.test.verify.definition
import org.koin.test.verify.injectedParameters
import org.koin.test.verify.verify

/**
 * Tests verifying the Koin configuration is correct.
 * For more information see:
 *   https://insert-koin.io/docs/reference/koin-test/verify#verifying-with-injected-parameters---jvm-only-40
 *   https://insert-koin.io/docs/reference/koin-test/verify#type-white-listing
 */
class JvmKoinTest : FunSpec({

    val koinTestModule = module {
        includes(koinModules)
    }

    test("Check Koin configuration") {
        koinTestModule.verify(
            extraTypes = listOf(DataStore::class),
            injections = injectedParameters(
                definition<MappingRemoteDataSource>(HttpClient::class, Json::class)
            )
        )
    }
})
