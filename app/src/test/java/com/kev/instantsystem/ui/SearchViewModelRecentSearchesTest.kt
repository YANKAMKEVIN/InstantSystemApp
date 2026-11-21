package com.kev.instantsystem.ui

import com.kev.instantsystem.data.search.RecentSearchesRepository
import com.kev.instantsystem.domain.usecase.SearchEverythingUseCase
import com.kev.instantsystem.testutils.InMemoryRecentSearchStorage
import com.kev.instantsystem.ui.search.SearchViewModel
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/** Plain JVM test (no Robolectric): recent searches survive a new launch of the app. */
@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelRecentSearchesTest {

    private val storage = InMemoryRecentSearchStorage()

    private fun newViewModel() = SearchViewModel(
        mockk<SearchEverythingUseCase>(relaxed = true),
        RecentSearchesRepository(storage)
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `recent searches are back after the app is reopened`() {
        newViewModel().apply {
            onQueryChanged("bitcoin")
            onSearchSubmitted()
            onSuggestionSelected("Climat")
        }

        assertEquals(listOf("Climat", "bitcoin"), newViewModel().recentSearches.value)
    }

    @Test
    fun `only the 5 latest searches are kept between launches`() {
        newViewModel().apply {
            (1..7).forEach { onSuggestionSelected("query $it") }
        }

        assertEquals((7 downTo 3).map { "query $it" }, newViewModel().recentSearches.value)
    }

    @Test
    fun `a query too short to be searched is not recorded`() {
        newViewModel().apply {
            onSuggestionSelected("bitcoin")
            onQueryChanged("ai")
            onSearchSubmitted()
        }

        assertEquals(listOf("bitcoin"), newViewModel().recentSearches.value)
    }
}
