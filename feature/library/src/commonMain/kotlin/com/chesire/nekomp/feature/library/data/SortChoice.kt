package com.chesire.nekomp.feature.library.data

import com.chesire.nekomp.core.resources.NekoRes
import nekomp.core.resources.generated.resources.library_sort_default
import nekomp.core.resources.generated.resources.library_sort_end_date
import nekomp.core.resources.generated.resources.library_sort_rating
import nekomp.core.resources.generated.resources.library_sort_start_date
import nekomp.core.resources.generated.resources.library_sort_title
import org.jetbrains.compose.resources.StringResource

enum class SortChoice(val displayString: StringResource) {
    Default(NekoRes.string.library_sort_default),
    Title(NekoRes.string.library_sort_title),
    StartDate(NekoRes.string.library_sort_start_date),
    EndDate(NekoRes.string.library_sort_end_date),
    Rating(NekoRes.string.library_sort_rating);

    companion object {

        internal val default: SortChoice = Default

        fun fromString(input: String): SortChoice {
            return SortChoice.entries.find { it.name.lowercase() == input.lowercase() } ?: default
        }
    }
}
