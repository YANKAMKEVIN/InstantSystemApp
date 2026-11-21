package com.kev.instantsystem.ui.utils

import java.time.Duration
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
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

/**
 * Short publication age for the article cards: "il y a 2 h" / "2 h ago", then the day once the
 * article is more than a week old. Returns an empty string when the date cannot be read.
 */
fun formatRelativeTime(
    publishedAt: String,
    now: Instant = Instant.now(),
    locale: Locale = Locale.getDefault()
): String {
    val published = try {
        OffsetDateTime.parse(publishedAt).toInstant()
    } catch (e: Exception) {
        return ""
    }
    val french = locale.language == "fr"
    val minutes = Duration.between(published, now).toMinutes()
    val hours = minutes / 60
    val days = hours / 24
    return when {
        minutes < 1 -> if (french) "à l’instant" else "just now"
        minutes < 60 -> if (french) "il y a $minutes min" else "$minutes min ago"
        hours < 24 -> if (french) "il y a $hours h" else "$hours h ago"
        days < 7 -> if (french) "il y a $days j" else "$days d ago"
        else -> DateTimeFormatter
            .ofPattern(if (french) "d MMM" else "MMM d", locale)
            .format(published.atZone(ZoneId.of("Europe/Paris")))
    }
}

/** "Le Monde · il y a 2 h", leaving out whichever part is missing. */
fun articleByline(sourceName: String, relativeTime: String): String =
    listOf(sourceName, relativeTime).filter { it.isNotBlank() }.joinToString(" · ")

/**
 * Date of the cached top headlines shown offline: "21 nov. à 18:04" / "Nov 21 at 18:04".
 */
fun formatCacheDate(
    epochMillis: Long,
    locale: Locale = Locale.getDefault(),
    zone: ZoneId = ZoneId.systemDefault()
): String = DateTimeFormatter
    .ofPattern(if (locale.language == "fr") "d MMM 'à' HH:mm" else "MMM d 'at' HH:mm", locale)
    .format(Instant.ofEpochMilli(epochMillis).atZone(zone))
