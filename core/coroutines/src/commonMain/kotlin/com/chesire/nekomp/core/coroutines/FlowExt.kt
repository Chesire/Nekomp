@file:OptIn(ExperimentalCoroutinesApi::class)

package com.chesire.nekomp.core.coroutines

import kotlin.time.Duration
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transformLatest

/**
 * Emits the latest value on the flow every [interval].
 */
fun <T> Flow<T>.emitLatestPeriodically(interval: Duration): Flow<T> = transformLatest {
    while (true) {
        emit(it)
        delay(interval)
    }
}
