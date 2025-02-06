package com.chesire.nekomp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform