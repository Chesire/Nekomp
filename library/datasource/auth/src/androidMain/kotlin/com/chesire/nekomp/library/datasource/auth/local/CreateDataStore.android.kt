package com.chesire.nekomp.library.datasource.auth.local

import android.content.Context
import org.koin.java.KoinJavaComponent.get

internal actual fun producePath(): String {
    return get<Context>(Context::class.java).filesDir.resolve(dataStoreFileName).absolutePath
}
