package com.kev.instantsystem.data

import com.kev.instantsystem.data.search.RecentSearchesRepository
import com.kev.instantsystem.testutils.InMemoryRecentSearchStorage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** Plain JVM test (no Robolectric). */
class RecentSearchesRepositoryTest {

    @Test
    fun `saved searches come back in the same order`() {
        val storage = InMemoryRecentSearchStorage()
        RecentSearchesRepository(storage).save(listOf("climat", "bitcoin"))

        assertEquals(listOf("climat", "bitcoin"), RecentSearchesRepository(storage).load())
    }

    @Test
    fun `only the 5 latest searches are kept`() {
        val storage = InMemoryRecentSearchStorage()
        val repository = RecentSearchesRepository(storage)

        repository.save((1..7).map { "query $it" })

        assertEquals((1..5).map { "query $it" }, repository.load())
    }

    @Test
    fun `nothing saved or unreadable storage starts empty`() {
        assertTrue(RecentSearchesRepository(InMemoryRecentSearchStorage()).load().isEmpty())
        assertTrue(RecentSearchesRepository(InMemoryRecentSearchStorage("not json")).load().isEmpty())
    }
}
