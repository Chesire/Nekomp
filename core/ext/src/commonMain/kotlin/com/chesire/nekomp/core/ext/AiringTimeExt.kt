package com.chesire.nekomp.core.ext

import com.chesire.nekomp.core.model.AiringTime
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn

fun AiringTime.airingAt(): LocalDateTime {
    val airingTimeZone = TimeZone.of(timeZone)

    val nowInAiringTimeZone = Clock.System.todayIn(airingTimeZone)
    val nextAirDay = nowInAiringTimeZone.nextDateWithWeekDay(dayOfWeek)
    val nextAirTime = nextAirDay.atTime(hour = hour, minute = minute)
    val nextAirTimeInstant = nextAirTime.toInstant(airingTimeZone)

    return nextAirTimeInstant.toLocalDateTime(TimeZone.currentSystemDefault())
}

@Suppress("MagicNumber")
private fun LocalDate.nextDateWithWeekDay(newDayOfWeek: DayOfWeek): LocalDate =
    plus((newDayOfWeek.isoDayNumber - dayOfWeek.isoDayNumber).mod(7), DateTimeUnit.DAY)
