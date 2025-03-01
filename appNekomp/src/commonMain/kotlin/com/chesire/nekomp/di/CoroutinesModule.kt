@file:OptIn(DelicateCoroutinesApi::class)

package com.chesire.nekomp.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import org.koin.dsl.module

val coroutinesModule = module {
    single<CoroutineScope> { GlobalScope }
}
