package com.kev.instantsystem.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kev.instantsystem.data.local.HeadlineArticleEntity
import com.kev.instantsystem.data.local.HeadlineListEntity
import com.kev.instantsystem.data.local.HeadlinesDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/**
 * In-memory [HeadlinesDao] for plain JVM tests: the SQL queries are replaced by list operations,
 * while [HeadlinesDao.replaceList] and [HeadlinesDao.appendPage] (the cache rules) stay the real ones.
 */
class FakeHeadlinesDao : HeadlinesDao() {
    val articles = mutableListOf<HeadlineArticleEntity>()
    private val lists = MutableStateFlow<Map<String, HeadlineListEntity>>(emptyMap())
    private var nextId = 1L

    fun articlesOf(cacheKey: String) = articles.filter { it.cacheKey == cacheKey }.sortedBy { it.position }

    override fun pagingSource(cacheKey: String): PagingSource<Int, HeadlineArticleEntity> =
        object : PagingSource<Int, HeadlineArticleEntity>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HeadlineArticleEntity> =
                LoadResult.Page(articlesOf(cacheKey), prevKey = null, nextKey = null)

            override fun getRefreshKey(state: PagingState<Int, HeadlineArticleEntity>): Int? = null
        }

    override suspend fun list(cacheKey: String): HeadlineListEntity? = lists.value[cacheKey]

    override fun updatedAt(cacheKey: String): Flow<Long?> = lists.map { it[cacheKey]?.updatedAt }

    override suspend fun lastPosition(cacheKey: String): Int = articlesOf(cacheKey).maxOfOrNull { it.position } ?: -1

    override suspend fun insertArticles(articles: List<HeadlineArticleEntity>) {
        this.articles += articles.map { it.copy(id = nextId++) }
    }

    override suspend fun upsertList(list: HeadlineListEntity) {
        lists.value = lists.value + (list.cacheKey to list)
    }

    override suspend fun deleteArticles(cacheKey: String) {
        articles.removeAll { it.cacheKey == cacheKey }
    }
}
