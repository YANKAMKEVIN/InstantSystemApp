package com.kev.instantsystem.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.kev.instantsystem.domain.model.Article
import kotlinx.coroutines.flow.Flow

/** Cached top headlines (only the "À la une" screen is cached). */
@Dao
abstract class HeadlinesDao {

    @Query("SELECT * FROM headline_articles WHERE cacheKey = :cacheKey ORDER BY position ASC")
    abstract fun pagingSource(cacheKey: String): PagingSource<Int, HeadlineArticleEntity>

    @Query("SELECT * FROM headline_lists WHERE cacheKey = :cacheKey")
    abstract suspend fun list(cacheKey: String): HeadlineListEntity?

    /** When the cached list was received (epoch milliseconds), or null when there is none. */
    @Query("SELECT updatedAt FROM headline_lists WHERE cacheKey = :cacheKey")
    abstract fun updatedAt(cacheKey: String): Flow<Long?>

    @Query("SELECT COALESCE(MAX(position), -1) FROM headline_articles WHERE cacheKey = :cacheKey")
    abstract suspend fun lastPosition(cacheKey: String): Int

    @Insert
    abstract suspend fun insertArticles(articles: List<HeadlineArticleEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun upsertList(list: HeadlineListEntity)

    @Query("DELETE FROM headline_articles WHERE cacheKey = :cacheKey")
    abstract suspend fun deleteArticles(cacheKey: String)

    /** A fresh first page replaces the whole cached list. */
    @Transaction
    open suspend fun replaceList(cacheKey: String, articles: List<Article>, nextPage: Int?, updatedAt: Long) {
        deleteArticles(cacheKey)
        insertArticles(articles.mapIndexed { index, article -> article.toHeadlineEntity(cacheKey, index) })
        upsertList(HeadlineListEntity(cacheKey = cacheKey, nextPage = nextPage, updatedAt = updatedAt))
    }

    /** A next page goes after the cached articles; the list keeps the date of its first page. */
    @Transaction
    open suspend fun appendPage(cacheKey: String, articles: List<Article>, nextPage: Int?) {
        val list = list(cacheKey) ?: return
        val start = lastPosition(cacheKey) + 1
        insertArticles(articles.mapIndexed { index, article -> article.toHeadlineEntity(cacheKey, start + index) })
        upsertList(list.copy(nextPage = nextPage))
    }
}
