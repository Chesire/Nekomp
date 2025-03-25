package com.chesire.nekomp.library.datasource.airing

import com.chesire.nekomp.core.model.Image
import com.chesire.nekomp.core.model.Season
import com.chesire.nekomp.core.model.Titles
import kotlinx.datetime.DayOfWeek

data class AiringAnime(
    val malId: Int,
    val kitsuId: Int?,
    val titles: Titles,
    val posterImage: Image,
    val airing: Boolean,
    val season: Season,
    val year: Int,
    val airingTime: AiringTime?
)

data class AiringTime(
    val dayOfWeek: DayOfWeek,
    val hour: Int,
    val minute: Int,
    val timeZone: String
)
