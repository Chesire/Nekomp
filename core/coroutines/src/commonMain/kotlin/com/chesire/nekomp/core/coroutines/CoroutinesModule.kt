@file:OptIn(DelicateCoroutinesApi::class)

package com.chesire.nekomp.core.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coroutinesModule = module {
    single<CoroutineScope> { GlobalScope }
    singleOf(::ProdDispatcher).bind(Dispatcher::class)
}
