package com.kev.instantsystem.ui

import androidx.paging.LoadState
import androidx.paging.PagingData
import com.kev.instantsystem.R
import com.kev.instantsystem.data.network.NetworkError
import com.kev.instantsystem.data.network.NetworkException
import com.kev.instantsystem.data.search.RecentSearchesRepository
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.domain.usecase.GetTopHeadlinesUseCase
import com.kev.instantsystem.domain.usecase.SearchEverythingUseCase
import com.kev.instantsystem.testutils.InMemoryRecentSearchStorage
import com.kev.instantsystem.ui.components.RefreshFailure
import com.kev.instantsystem.ui.components.refreshErrorMessageRes
import com.kev.instantsystem.ui.headlines.ArticleCategory
import com.kev.instantsystem.ui.headlines.HeadlinesViewModel
import com.kev.instantsystem.ui.home.HOME_LIST_KEY
import com.kev.instantsystem.ui.home.HomeViewModel
import com.kev.instantsystem.ui.search.SEARCH_LIST_KEY
import com.kev.instantsystem.ui.search.SearchViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

/**
 * Plain JVM test (no Robolectric): a reload that fails while articles are on screen is reported
 * once to the screen (message at the bottom with "Retry").
 */
@OptIn(ExperimentalCoroutinesApi::class)
class RefreshFailureViewModelTest {

    private val headlinesUseCase = mockk<GetTopHeadlinesUseCase>()
    private val searchUseCase = mockk<SearchEverythingUseCase>()
    private val sports = ArticleCategory.Sports.listKey
    private val latest = ArticleCategory.Latest.listKey

    private fun offline() = LoadState.Error(NetworkException(NetworkError.UnResolveAddress("offline")))

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        every { headlinesUseCase(country = any(), category = any(), query = any()) } returns
            flowOf(PagingData.empty<Article>())
        every { headlinesUseCase.updatedAt(country = any(), category = any()) } returns flowOf(null)
        every {
            searchUseCase(query = any(), sortBy = any(), from = any(), to = any(), language = any())
        } returns flowOf(PagingData.empty<Article>())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun headlines() = HeadlinesViewModel(headlinesUseCase)

    @Test
    fun `a failed reload with articles on screen is reported for that list`() {
        val viewModel = headlines()
        val failed = offline()

        viewModel.onListLoadState(sports, failed, itemCount = 20)

        assertEquals(RefreshFailure(sports, failed.error), viewModel.refreshFailure.value)
    }

    @Test
    fun `with no article on screen the error screen is used instead`() {
        val viewModel = headlines()

        viewModel.onListLoadState(sports, offline(), itemCount = 0)

        assertNull(viewModel.refreshFailure.value)
    }

    @Test
    fun `loading or a successful reload reports nothing`() {
        val viewModel = headlines()

        viewModel.onListLoadState(sports, LoadState.Loading, itemCount = 20)
        viewModel.onListLoadState(sports, LoadState.NotLoading(endOfPaginationReached = false), itemCount = 20)

        assertNull(viewModel.refreshFailure.value)
    }

    @Test
    fun `once shown, the same failure is not shown again after a rotation or a tab switch`() {
        val viewModel = headlines()
        val failed = offline()
        viewModel.onListLoadState(sports, failed, itemCount = 20)

        viewModel.onRefreshFailureShown(viewModel.refreshFailure.value!!)
        viewModel.onListLoadState(sports, failed, itemCount = 20)

        assertNull(viewModel.refreshFailure.value)
    }

    @Test
    fun `a retry that fails again is reported again`() {
        val viewModel = headlines()
        viewModel.onListLoadState(sports, offline(), itemCount = 20)
        viewModel.onRefreshFailureShown(viewModel.refreshFailure.value!!)

        val again = offline()
        viewModel.onListLoadState(sports, LoadState.Loading, itemCount = 20)
        viewModel.onListLoadState(sports, again, itemCount = 20)

        assertEquals(RefreshFailure(sports, again.error), viewModel.refreshFailure.value)
    }

    @Test
    fun `marking an older failure as shown keeps the newer one`() {
        val viewModel = headlines()
        viewModel.onListLoadState(sports, offline(), itemCount = 20)
        val older = viewModel.refreshFailure.value!!
        val newer = offline()
        viewModel.onListLoadState(latest, newer, itemCount = 20)

        viewModel.onRefreshFailureShown(older)

        assertEquals(RefreshFailure(latest, newer.error), viewModel.refreshFailure.value)
    }

    @Test
    fun `the home and search lists report their failed reloads too`() {
        val home = HomeViewModel(searchUseCase)
        val search = SearchViewModel(searchUseCase, RecentSearchesRepository(InMemoryRecentSearchStorage()))
        val failed = offline()

        home.onListLoadState(HOME_LIST_KEY, failed, itemCount = 3)
        search.onListLoadState(SEARCH_LIST_KEY, failed, itemCount = 3)

        assertEquals(RefreshFailure(HOME_LIST_KEY, failed.error), home.refreshFailure.value)
        assertEquals(RefreshFailure(SEARCH_LIST_KEY, failed.error), search.refreshFailure.value)
    }

    @Test
    fun `the bottom message says why the reload failed`() {
        assertEquals(R.string.refresh_error_offline, refreshErrorMessageRes(offline().error))
        assertEquals(
            R.string.refresh_error_rate_limited,
            refreshErrorMessageRes(NetworkException(NetworkError.TooManyRequests("HTTP 429")))
        )
        assertEquals(
            R.string.refresh_error,
            refreshErrorMessageRes(NetworkException(NetworkError.InternalServerError("HTTP 500")))
        )
        assertEquals(R.string.refresh_error, refreshErrorMessageRes(IllegalStateException("boom")))
    }
}
