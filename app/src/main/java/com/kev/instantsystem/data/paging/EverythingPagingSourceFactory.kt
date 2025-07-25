package com.kev.instantsystem.data.paging

import androidx.paging.PagingSource
import com.kev.instantsystem.data.remote.datasource.NewsRemoteDataSource
import com.kev.instantsystem.domain.model.Article

/**
 * Factory class to create instances of [EverythingPagingSource].
 *
 * Useful to keep construction logic clean and injectable in the repository.
 */
open class EverythingPagingSourceFactory(
    private val dataSource: NewsRemoteDataSource
) {
    open fun create(
        query: String,
        sortBy: String?,
        from: String?,
        to: String?,
        language: String?
    ): PagingSource<Int, Article> {
        return EverythingPagingSource(
            dataSource = dataSource,
            query = query,
            sortBy = sortBy,
            from = from,
            to = to,
            language = language
        )
    }
}
