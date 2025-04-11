@file:OptIn(KoinExperimentalAPI::class)

package com.chesire.nekomp

import androidx.datastore.core.DataStore
import io.kotest.core.spec.style.FunSpec
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
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
            injections = injectedParameters()
        )
    }
})
