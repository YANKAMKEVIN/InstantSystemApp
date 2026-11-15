package com.kev.instantsystem.data.remote.datasource

import com.kev.instantsystem.BuildConfig
import com.kev.instantsystem.data.dto.NewsDto
import com.kev.instantsystem.data.network.NetworkError
import com.kev.instantsystem.data.network.NetworkResponse
import com.kev.instantsystem.data.network.NetworkUtil
import com.kev.instantsystem.data.remote.NewsApiService
import javax.inject.Inject

class NewsRemoteDataSourceImpl(
    private val apiService: NewsApiService,
    private val apiKey: String
) : NewsRemoteDataSource {

    @Inject
    constructor(apiService: NewsApiService) : this(apiService, BuildConfig.NEWS_API_KEY)

    /**
     * Without a key NewsAPI answers 401 to every call and the user only sees "HTTP 401".
     * Fail early with an explicit message instead (the build falls back to [MISSING_API_KEY]).
     */
    private fun <T : Any> missingApiKey(): NetworkResponse<T>? =
        if (apiKey.isBlank() || apiKey == MISSING_API_KEY) {
            NetworkResponse.Failure(NetworkError.MissingApiKey(MISSING_API_KEY_MESSAGE))
        } else {
            null
        }

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
        missingApiKey<NewsDto>()?.let { return it }
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
        missingApiKey<NewsDto>()?.let { return it }
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

    companion object {
        /** Value of BuildConfig.NEWS_API_KEY when local.properties has no NEWS_API_KEY (see app/build.gradle.kts). */
        const val MISSING_API_KEY = "MISSING_API_KEY"

        const val MISSING_API_KEY_MESSAGE =
            "Clé API NewsAPI manquante : ajoutez NEWS_API_KEY=<votre clé> dans local.properties, " +
                "puis recompilez l’application (voir le README)."
    }
}
