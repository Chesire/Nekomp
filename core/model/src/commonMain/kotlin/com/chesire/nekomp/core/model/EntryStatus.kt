package com.chesire.nekomp.core.model

enum class EntryStatus {
    Current,
    OnHold,
    Planned,
    Completed,
    Dropped;

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
