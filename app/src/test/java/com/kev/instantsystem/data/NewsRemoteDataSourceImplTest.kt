package com.kev.instantsystem.data

import com.kev.instantsystem.data.dto.NewsDto
import com.kev.instantsystem.data.network.NetworkError
import com.kev.instantsystem.data.network.NetworkResponse
import com.kev.instantsystem.data.remote.NewsApiService
import com.kev.instantsystem.data.remote.datasource.NewsRemoteDataSourceImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

class NewsRemoteDataSourceImplTest {

    private val api = mockk<NewsApiService>()

    @Test
    fun `without an API key the calls fail with an explicit error and never hit the network`() = runTest {
        val dataSource = NewsRemoteDataSourceImpl(api, NewsRemoteDataSourceImpl.MISSING_API_KEY)

        val everything = dataSource.getEverything("technology", null, null, null, "fr", 1, 20)
        val headlines = dataSource.getTopHeadlines("us", null, null, 1, 20)

        listOf(everything, headlines).forEach { result ->
            assertTrue("was $result", result is NetworkResponse.Failure)
            val error = (result as NetworkResponse.Failure).error
            assertTrue(error is NetworkError.MissingApiKey)
            assertTrue(error.message.contains("NEWS_API_KEY"))
        }
        coVerify(exactly = 0) { api.getEverything(any(), any(), any(), any(), any(), any(), any(), any()) }
    }

    @Test
    fun `with an API key the request is sent`() = runTest {
        val dto = mockk<NewsDto>()
        coEvery { api.getEverything(any(), any(), any(), any(), any(), any(), any(), any()) } returns dto
        val dataSource = NewsRemoteDataSourceImpl(api, "real-key")

        val result = dataSource.getEverything("technology", null, null, null, "fr", 1, 20)

        assertTrue("was $result", result is NetworkResponse.Success)
    }
}
