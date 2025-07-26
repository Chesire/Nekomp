package com.chesire.nekomp.core.preferences.models

enum class ImageQuality {
    Lowest,
    Low,
    Medium,
    High,
    Highest;

    companion object {

        internal val default: ImageQuality = Medium

        fun fromString(input: String): ImageQuality {
            return ImageQuality.entries.find { it.name.lowercase() == input.lowercase() } ?: default
        }
    }
}
