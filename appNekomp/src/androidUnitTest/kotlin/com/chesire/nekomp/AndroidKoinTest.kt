@file:OptIn(KoinExperimentalAPI::class)

package com.chesire.nekomp

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.work.WorkerParameters
import com.chesire.nekomp.di.koinAndroidModules
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
class AndroidKoinTest : FunSpec({

    val koinTestModule = module {
        includes(koinModules)
        includes(koinAndroidModules)
    }

    test("Check Koin configuration") {
        koinTestModule.verify(
            extraTypes = listOf(
                Context::class, // Part of Android sdk,
                DataStore::class, // Built as needed
                WorkerParameters::class // Injected as part of WorkManager
            ),
            injections = injectedParameters()
        )
    }
})
