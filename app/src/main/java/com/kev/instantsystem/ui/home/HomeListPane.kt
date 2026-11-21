package com.kev.instantsystem.ui.home

import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.ui.components.PagedArticlesContent
import com.kev.instantsystem.ui.components.RefreshFailureReporter

@Composable
fun HomeListPane(
    articles: LazyPagingItems<Article>,
    onArticleClick: (Article) -> Unit,
    onArticleLongClick: ((Article) -> Unit)? = null,
    refreshFailures: RefreshFailureReporter? = null
) {
    PagedArticlesContent(
        articles = articles,
        refreshFailures = refreshFailures,
        listKey = HOME_LIST_KEY,
        onArticleClick = onArticleClick,
        onArticleLongClick = onArticleLongClick
    )
}

const val HOME_LIST_KEY = "home"
