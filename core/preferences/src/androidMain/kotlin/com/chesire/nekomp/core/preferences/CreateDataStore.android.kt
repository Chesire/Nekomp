package com.chesire.nekomp.core.preferences

import android.content.Context
import org.koin.java.KoinJavaComponent.get

internal actual fun producePath(filename: String): String {
    return get<Context>(Context::class.java).filesDir.resolve(filename).absolutePath
}
