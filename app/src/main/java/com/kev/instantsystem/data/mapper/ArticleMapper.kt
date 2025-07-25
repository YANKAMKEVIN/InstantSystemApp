package com.kev.instantsystem.data.mapper

import com.kev.instantsystem.data.dto.ArticleDto
import com.kev.instantsystem.data.dto.NewsDto
import com.kev.instantsystem.data.dto.SourceDto
import com.kev.ynfluence.ui.screens.news.ui.model.Article
import com.kev.ynfluence.ui.screens.news.ui.model.Source

fun ArticleDto.toDomain(): Article = Article(
    source = source.toDomain(),
    author = author,
    title = title,
    description = description,
    url = url,
    urlToImage = urlToImage,
    publishedAt = publishedAt,
    content = content
)

fun SourceDto.toDomain(): Source = Source(
    id = id,
    name = name
)

fun NewsDto.toDomainArticles(): List<Article> {
    return articles.map { it.toDomain() }
}