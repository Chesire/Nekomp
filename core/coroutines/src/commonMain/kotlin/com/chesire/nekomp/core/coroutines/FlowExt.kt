@file:OptIn(ExperimentalCoroutinesApi::class)

package com.chesire.nekomp.core.coroutines

import kotlin.time.Duration
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
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

/**
 * Performs a [combine] with 5 elements.
 */
@Suppress("MagicNumber")
inline fun <T1, T2, T3, T4, T5, R> combine(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    crossinline transform: suspend (T1, T2, T3, T4, T5) -> R
): Flow<R> {
    return combine(flow, flow2, flow3, flow4, flow5) { args: Array<*> ->
        @Suppress("UNCHECKED_CAST")
        transform(
            args[0] as T1,
            args[1] as T2,
            args[2] as T3,
            args[3] as T4,
            args[4] as T5
        )
    }
}

/**
 * Performs a [combine] with 6 elements.
 */
@Suppress("MagicNumber")
inline fun <T1, T2, T3, T4, T5, T6, R> combine(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    crossinline transform: suspend (T1, T2, T3, T4, T5, T6) -> R
): Flow<R> {
    return combine(flow, flow2, flow3, flow4, flow5, flow6) { args: Array<*> ->
        @Suppress("UNCHECKED_CAST")
        transform(
            args[0] as T1,
            args[1] as T2,
            args[2] as T3,
            args[3] as T4,
            args[4] as T5,
            args[5] as T6,
        )
    }
}
