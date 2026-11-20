package com.kev.instantsystem.ui.home

import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.ui.components.PagedArticlesContent

@Composable
fun HomeListPane(
    articles: LazyPagingItems<Article>,
    onArticleClick: (Article) -> Unit,
    onArticleLongClick: ((Article) -> Unit)? = null
) {
    PagedArticlesContent(
        articles = articles,
        onArticleClick = onArticleClick,
        onArticleLongClick = onArticleLongClick
    )
}
