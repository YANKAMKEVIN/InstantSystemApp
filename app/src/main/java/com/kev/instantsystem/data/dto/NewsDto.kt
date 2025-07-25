package com.kev.instantsystem.data.dto

import com.kev.instantsystem.data.dto.ArticleDto

data class NewsDto(
    val status: String,
    val totalResults: Int,
    val articles: List<ArticleDto>
)
