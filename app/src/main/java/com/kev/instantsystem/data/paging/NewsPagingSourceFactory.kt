package com.kev.instantsystem.data.paging

import androidx.paging.PagingSource
import com.kev.instantsystem.data.remote.datasource.NewsRemoteDataSource
import com.kev.instantsystem.domain.model.Article
import javax.inject.Inject

/**
 * Factory class to create instances of [NewsPagingSourceFactory].
 *
 * Useful to keep construction logic clean and injectable in the repository.
 */
open class NewsPagingSourceFactory @Inject constructor(
    private val remoteDataSource: NewsRemoteDataSource,
) {
    open fun create(
        country: String,
        category: String?,
        query: String?
    ): PagingSource<Int, Article> {
        return NewsPagingSource(
            remoteDataSource = remoteDataSource,
            country = country,
            category = category,
            query = query
        )
    }
}
