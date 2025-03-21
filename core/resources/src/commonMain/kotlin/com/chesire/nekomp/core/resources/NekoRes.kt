@file:OptIn(ExperimentalResourceApi::class)

package com.chesire.nekomp.core.resources

import nekomp.core.resources.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

// This saves having to add a custom import every class that uses resources
object NekoRes {
    val string = Res.string
    val plurals = Res.plurals
    val drawable = Res.drawable

    suspend fun readBytes(path: String) = Res.readBytes(path)
}
