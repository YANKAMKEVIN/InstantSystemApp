package com.kev.instantsystem.ui.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant
import java.util.Locale

class RelativeTimeTest {

    private val now = Instant.parse("2025-07-24T14:00:00Z")

    private fun fr(publishedAt: String) = formatRelativeTime(publishedAt, now, Locale.FRENCH)
    private fun en(publishedAt: String) = formatRelativeTime(publishedAt, now, Locale.ENGLISH)

    @Test
    fun `recent articles show minutes or hours`() {
        assertEquals("à l’instant", fr("2025-07-24T13:59:30Z"))
        assertEquals("il y a 5 min", fr("2025-07-24T13:55:00Z"))
        assertEquals("il y a 2 h", fr("2025-07-24T12:00:00Z"))
        assertEquals("2 h ago", en("2025-07-24T12:00:00Z"))
        assertEquals("just now", en("2025-07-24T14:00:00Z"))
    }

    @Test
    fun `older articles show days, then the date`() {
        assertEquals("il y a 3 j", fr("2025-07-21T10:00:00Z"))
        assertEquals("3 d ago", en("2025-07-21T10:00:00Z"))
        assertTrue(fr("2025-07-01T10:00:00Z").startsWith("1 "))
        assertTrue(en("2025-07-01T10:00:00Z").endsWith(" 1"))
    }

    @Test
    fun `dates with an offset and future dates are handled`() {
        assertEquals("il y a 1 h", fr("2025-07-24T15:00:00+02:00"))
        assertEquals("à l’instant", fr("2025-07-24T15:00:00Z"))
    }

    @Test
    fun `unreadable dates give an empty text`() {
        assertEquals("", fr("2025-07-24"))
        assertEquals("", fr(""))
    }

    @Test
    fun `byline joins source and time and skips what is missing`() {
        assertEquals("Le Monde · il y a 2 h", articleByline("Le Monde", "il y a 2 h"))
        assertEquals("Le Monde", articleByline("Le Monde", ""))
        assertEquals("il y a 2 h", articleByline("", "il y a 2 h"))
    }
}
