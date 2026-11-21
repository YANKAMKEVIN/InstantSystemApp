package com.kev.instantsystem.ui.components

import com.kev.instantsystem.R
import com.kev.instantsystem.data.network.NetworkError
import com.kev.instantsystem.data.network.NetworkException
import org.junit.Assert.assertEquals
import org.junit.Test

class ErrorMessagesTest {

    @Test
    fun `known network errors get their own message`() {
        assertEquals(R.string.error_missing_api_key, errorMessageRes(NetworkException(NetworkError.MissingApiKey("x"))))
        assertEquals(R.string.error_invalid_api_key, errorMessageRes(NetworkException(NetworkError.Unauthorized("HTTP 401"))))
        assertEquals(R.string.error_rate_limited, errorMessageRes(NetworkException(NetworkError.TooManyRequests("HTTP 429"))))
        assertEquals(R.string.error_offline, errorMessageRes(NetworkException(NetworkError.UnResolveAddress("no host"))))
    }

    @Test
    fun `other errors get the generic message, never the raw text`() {
        assertEquals(R.string.error_message, errorMessageRes(NetworkException(NetworkError.InternalServerError("HTTP 500"))))
        assertEquals(R.string.error_message, errorMessageRes(IllegalStateException("boom")))
        assertEquals(R.string.error_message, errorMessageRes(null))
    }
}
