package com.chesire.nekomp.core.ext

/**
 * This replaces the usage of the deprecated capitalize method on String.
 */
fun String.capitalize(): String {
    return replaceFirstChar {
        if (it.isLowerCase()) it.titlecase() else it.toString()
    }
}
