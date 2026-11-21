package com.kev.instantsystem.data

import com.kev.instantsystem.data.network.NetworkError
import com.kev.instantsystem.data.network.NetworkResponse
import com.kev.instantsystem.data.network.NetworkUtil
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.net.UnknownHostException

class NetworkUtilTest {

    private suspend fun failureOf(block: suspend () -> Any): NetworkError {
        val response = NetworkUtil.executeApiCall(block)
        assertTrue(response is NetworkResponse.Failure)
        return (response as NetworkResponse.Failure).error
    }

    private fun httpError(code: Int) = HttpException(Response.error<Any>(code, "".toResponseBody(null)))

    @Test
    fun `no connection is reported as an unreachable address`() = runTest {
        val error = failureOf { throw UnknownHostException("Unable to resolve host newsapi.org") }
        assertTrue(error is NetworkError.UnResolveAddress)
    }

    @Test
    fun `HTTP 429 is reported as too many requests`() = runTest {
        assertTrue(failureOf { throw httpError(429) } is NetworkError.TooManyRequests)
    }

    @Test
    fun `HTTP 401 is still reported as unauthorized`() = runTest {
        assertTrue(failureOf { throw httpError(401) } is NetworkError.Unauthorized)
    }
}
