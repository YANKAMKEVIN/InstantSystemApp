package com.kev.instantsystem.data.dto

data class NewsDto(
    val status: String,
    val totalResults: Int,
    val articles: List<ArticleDto>
)
