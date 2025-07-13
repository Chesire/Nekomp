package com.chesire.nekomp.library.datasource.library

import com.chesire.nekomp.core.model.EntryStatus
import com.chesire.nekomp.core.model.Image
import com.chesire.nekomp.core.model.Titles
import com.chesire.nekomp.core.model.Type
import kotlinx.datetime.Instant

data class LibraryEntry(
    // Id of the database entry in the API
    val id: Int,
    // Id of the item within the users library
    val entryId: Int,
    val type: Type,
    val updatedAt: Instant,
    val primaryType: String, // Enum
    val subtype: String, // Enum
    val slug: String,
    val titles: Titles,
    val seriesStatus: String, // Enum
    val entryStatus: EntryStatus,
    val progress: Int,
    val totalLength: Int,
    val rating: Int,
    val posterImage: Image,
    val coverImage: Image,
    val startDate: String,
    val endDate: String
) {

    /**
     * Gets a string that can be used to display the total length to the user.
     */
    val displayTotalLength = if (totalLength == 0) "-" else totalLength

    /**
     * Gets the percent of the way through the entry the user is.
     */
    val progressPercent = if (totalLength == 0) {
        0f
    } else {
        (progress.toFloat() / totalLength.toFloat())
    }

    /**
     * Gets if the progress for this series can be incremented.
     */
    val canIncrementProgress = if (totalLength == 0) true else progress < totalLength
}
