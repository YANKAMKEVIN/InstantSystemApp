package com.kev.instantsystem.data.remote.datasource

import com.kev.instantsystem.BuildConfig
import com.kev.instantsystem.data.dto.NewsDto
import com.kev.instantsystem.data.network.NetworkResponse
import com.kev.instantsystem.data.network.NetworkUtil
import com.kev.instantsystem.data.remote.NewsApiService
import javax.inject.Inject

class NewsRemoteDataSourceImpl @Inject constructor(
    private val apiService: NewsApiService
) : NewsRemoteDataSource {

    private val apiKey = BuildConfig.NEWS_API_KEY

    /**
     * Calls the News API to fetch top headlines.
     * Uses NetworkUtil to wrap the result into a NetworkResponse.
     */
    override suspend fun getTopHeadlines(
        country: String?,
        category: String?,
        query: String?,
        page: Int,
        pageSize: Int
    ): NetworkResponse<NewsDto> {
        return NetworkUtil.executeApiCall {
            apiService.getTopHeadlines(
                country = country,
                category = category,
                query = query,
                page = page,
                pageSize = pageSize,
                apiKey = apiKey
            )
        }
    }

    /**
     * Calls the News API to search all articles using the "everything" endpoint.
     * Supports advanced querying and filtering.
     */
    override suspend fun getEverything(
        query: String,
        sortBy: String?,
        from: String?,
        to: String?,
        language: String?,
        page: Int,
        pageSize: Int
    ): NetworkResponse<NewsDto> {
        return NetworkUtil.executeApiCall {
            apiService.getEverything(
                apiKey = apiKey,
                query = query,
                sortBy = sortBy,
                from = from,
                to = to,
                language = language,
                page = page,
                pageSize = pageSize
            )
        }
    }
}
