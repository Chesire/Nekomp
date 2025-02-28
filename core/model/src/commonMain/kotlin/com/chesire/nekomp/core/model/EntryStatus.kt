package com.chesire.nekomp.core.model

enum class EntryStatus {
    Completed,
    Current,
    Dropped,
    OnHold,
    Planned;

    companion object {

        internal val default: EntryStatus = Current // If no other value

        fun fromString(input: String): EntryStatus {
            return if (input == "on_hold") {
                OnHold
            } else {
                EntryStatus.entries.find { it.name.lowercase() == input.lowercase() } ?: default
            }
        }
    }
}
