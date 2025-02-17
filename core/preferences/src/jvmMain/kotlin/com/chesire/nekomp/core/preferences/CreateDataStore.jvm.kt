package com.chesire.nekomp.core.preferences

import co.touchlab.kermit.Logger
import java.io.File

actual fun producePath(filename: String): String {
    val datastoreFile = File(System.getProperty("java.io.tmpdir"), filename)
    Logger.d { "Datastore file being made at ${datastoreFile.absolutePath}" }
    return datastoreFile.absolutePath
}
