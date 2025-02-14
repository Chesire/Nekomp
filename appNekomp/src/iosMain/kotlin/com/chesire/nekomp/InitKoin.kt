@file:OptIn(ExperimentalObjCRefinement::class)

package com.chesire.nekomp

import kotlin.experimental.ExperimentalObjCRefinement

/**
 * Initializes Koin for iOS without the lambda.
 */
fun initKoin() {
    initKoin { }
}
