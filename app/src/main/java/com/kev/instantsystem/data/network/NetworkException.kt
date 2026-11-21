package com.kev.instantsystem.data.network

/**
 * Carries a [NetworkError] through Paging's `LoadResult.Error`, so the screens can show a message
 * in the interface language instead of the raw text of the error ("HTTP 401", "Unable to resolve host"...).
 */
class NetworkException(val error: NetworkError) : Exception(error.message)
