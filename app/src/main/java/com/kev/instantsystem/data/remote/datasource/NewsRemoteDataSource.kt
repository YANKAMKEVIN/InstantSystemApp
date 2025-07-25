package com.kev.instantsystem.data.remote.datasource

import com.kev.instantsystem.data.dto.NewsDto
import com.kev.instantsystem.data.network.NetworkResponse

interface NewsRemoteDataSource {

    /**
     * Fetches top headlines from the News API.
     *
     * Useful for displaying a curated list of trending or important news.
     * Supports filtering by country, category, or search query.
     *
     * @param country ISO 3166-1 country code (e.g., "fr", "us")
     * @param category News category (e.g., "technology")
     * @param query Keyword or phrase to search for
     * @param page Page number for pagination
     * @param pageSize Number of results per page (max 100)
     */
    suspend fun getTopHeadlines(
        country: String? = null,
        category: String? = null,
        query: String? = null,
        page: Int,
        pageSize: Int,
    ): NetworkResponse<NewsDto>

    /**
     * Searches through all available news articles using a given query.
     *
     * This endpoint supports full-text search and is ideal for article discovery.
     *
     * @param query Search query (required)
     * @param sortBy Sorting method ("relevancy", "popularity", "publishedAt")
     * @param from Minimum publication date (ISO 8601 format)
     * @param to Maximum publication date (ISO 8601 format)
     * @param language Filter results by language (e.g., "fr", "en")
     * @param page Page number for pagination
     * @param pageSize Number of results per page (max 100)
     */
    suspend fun getEverything(
        query: String,
        sortBy: String? = null,
        from: String? = null,
        to: String? = null,
        language: String? = null,
        page: Int,
        pageSize: Int,
    ): NetworkResponse<NewsDto>
}