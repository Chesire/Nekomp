package com.chesire.nekomp.core.preferences.models

enum class Theme {
    System,
    Light,
    Dark;

    companion object {

        internal val default: Theme = System

        fun fromString(input: String): Theme {
            return Theme.entries.find { it.name.lowercase() == input.lowercase() } ?: default
        }
    }
}
