package com.kev.instantsystem.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.domain.model.Source

/**
 * A top headline kept on the device, so the last list received can be shown offline.
 * [cacheKey] says which list it belongs to (see [headlinesCacheKey]) and [position] keeps the API order.
 */
@Entity(
    tableName = "headline_articles",
    indices = [Index(value = ["cacheKey", "position"])]
)
data class HeadlineArticleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val cacheKey: String,
    val position: Int,
    val sourceId: String?,
    val sourceName: String,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?
)

/** One cached list: when it was received and which API page comes next (null at the end). */
@Entity(tableName = "headline_lists")
data class HeadlineListEntity(
    @PrimaryKey val cacheKey: String,
    val nextPage: Int?,
    val updatedAt: Long
)

/** One list per country and category ("us:" for the latest headlines, "us:sports"…). */
fun headlinesCacheKey(country: String, category: String?): String = "$country:${category.orEmpty()}"

fun Article.toHeadlineEntity(cacheKey: String, position: Int) = HeadlineArticleEntity(
    cacheKey = cacheKey,
    position = position,
    sourceId = source.id,
    sourceName = source.name,
    author = author,
    title = title,
    description = description,
    url = url,
    urlToImage = urlToImage,
    publishedAt = publishedAt,
    content = content
)

fun HeadlineArticleEntity.toDomain() = Article(
    source = Source(id = sourceId, name = sourceName),
    author = author,
    title = title,
    description = description,
    url = url,
    urlToImage = urlToImage,
    publishedAt = publishedAt,
    content = content
)
