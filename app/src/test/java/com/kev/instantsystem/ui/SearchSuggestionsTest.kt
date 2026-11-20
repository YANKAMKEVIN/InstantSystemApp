package com.kev.instantsystem.ui

import com.kev.instantsystem.domain.usecase.SearchEverythingUseCase
import com.kev.instantsystem.ui.search.SearchViewModel
import com.kev.instantsystem.ui.search.addRecentSearch
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

/** Plain JVM test (no Robolectric). */
@OptIn(ExperimentalCoroutinesApi::class)
class SearchSuggestionsTest {

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `recent searches keep the latest first, without duplicates`() {
        var recent = emptyList<String>()
        listOf("bitcoin", "climat", "Bitcoin ", "ai", "  ").forEach {
            recent = addRecentSearch(recent, it)
        }

        assertEquals(listOf("Bitcoin", "climat"), recent)
    }

    @Test
    fun `recent searches are capped`() {
        val recent = (1..8).fold(emptyList<String>()) { acc, i -> addRecentSearch(acc, "query $i", max = 5) }

        assertEquals(listOf("query 8", "query 7", "query 6", "query 5", "query 4"), recent)
    }

    @Test
    fun `submitting the query or picking a topic records it`() {
        val viewModel = SearchViewModel(mockk<SearchEverythingUseCase>(relaxed = true))

        viewModel.onQueryChanged("bitcoin")
        viewModel.onSearchSubmitted()
        viewModel.onSuggestionSelected("Climat")

        assertEquals("Climat", viewModel.query.value)
        assertEquals(listOf("Climat", "bitcoin"), viewModel.recentSearches.value)
    }
}
