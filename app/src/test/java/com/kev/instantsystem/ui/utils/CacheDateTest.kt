package com.kev.instantsystem.ui.utils

import androidx.paging.LoadState
import com.kev.instantsystem.ui.components.cachedListDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Locale

class CacheDateTest {

    private val paris = ZoneId.of("Europe/Paris")
    private val received = ZonedDateTime.of(2026, 11, 21, 18, 4, 0, 0, paris).toInstant().toEpochMilli()

    @Test
    fun `the cache date reads naturally in French and English`() {
        assertEquals("21 nov. à 18:04", formatCacheDate(received, Locale.FRENCH, paris))
        assertEquals("Nov 21 at 18:04", formatCacheDate(received, Locale.ENGLISH, paris))
    }

    @Test
    fun `the date is shown only when the list on screen could not be reloaded`() {
        val failed = LoadState.Error(Exception("offline"))

        assertEquals(received, cachedListDate(failed, itemCount = 5, cachedAt = received))
        assertNull(cachedListDate(LoadState.NotLoading(endOfPaginationReached = false), 5, received))
        assertNull(cachedListDate(LoadState.Loading, 5, received))
        assertNull(cachedListDate(failed, itemCount = 0, cachedAt = received))
        assertNull(cachedListDate(failed, itemCount = 5, cachedAt = null))
    }
}
