package com.chesire.nekomp.core.coroutines

import kotlinx.coroutines.CoroutineDispatcher

interface Dispatcher {

    val default: CoroutineDispatcher
    val io: CoroutineDispatcher
}
