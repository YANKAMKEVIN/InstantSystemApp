package com.kev.instantsystem.ui.utils

import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.TextStyle
import java.util.Locale

fun formatPublishedDate(publishedAt: String): String {
    return try {
        val locale = Locale.getDefault()
        val utcDateTime = OffsetDateTime.parse(publishedAt)
        val franceZone = ZoneId.of("Europe/Paris")
        val localDateTime = utcDateTime.atZoneSameInstant(franceZone)

        val now = ZonedDateTime.now(franceZone)
        val publishedDate = localDateTime.toLocalDate()
        val hour = localDateTime.hour.toString().padStart(2, '0')
        val minute = localDateTime.minute.toString().padStart(2, '0')

        return when (publishedDate) {
            now.toLocalDate() -> when (locale.language) {
                "fr" -> "Aujourd’hui à $hour:$minute"
                "en" -> "Today at $hour:$minute"
                else -> "Today at $hour:$minute"
            }

            now.minusDays(1).toLocalDate() -> when (locale.language) {
                "fr" -> "Hier à $hour:$minute"
                "en" -> "Yesterday at $hour:$minute"
                else -> "Yesterday at $hour:$minute"
            }

            else -> {
                val day = publishedDate.dayOfMonth
                val rawMonth = publishedDate.month.getDisplayName(TextStyle.FULL, locale)
                val month =
                    rawMonth.replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }
                when (locale.language) {
                    "fr" -> "Le $day $month à $hour:$minute"
                    "en" -> "On $month $day at $hour:$minute"
                    else -> "$day $month $hour:$minute"
                }
            }
        }
    } catch (e: Exception) {
        ""
    }
}
