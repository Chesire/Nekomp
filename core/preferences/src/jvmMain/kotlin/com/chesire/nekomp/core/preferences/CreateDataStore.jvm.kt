package com.chesire.nekomp.core.preferences

import java.io.File

actual fun producePath(filename: String): String {
    val datastoreFile = File(System.getProperty("java.io.tmpdir"), filename)
    return datastoreFile.absolutePath
}
