package com.kev.instantsystem.ui.home

import androidx.compose.runtime.Composable
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.ui.components.EmptyListPlaceholder
import com.kev.instantsystem.ui.components.LoaderScreen
import com.kev.instantsystem.ui.components.LoaderState
import com.kev.instantsystem.ui.components.NewsListScreenPaging

@Composable
fun HomeListPane(
    articles: LazyPagingItems<Article>,
    onArticleClick: (Article) -> Unit
) {
    when (articles.loadState.refresh) {
        is LoadState.Loading -> {
            LoaderScreen(state = LoaderState.Loading)
        }

        is LoadState.Error -> {
            val error = (articles.loadState.refresh as LoadState.Error).error
            LoaderScreen(
                state = LoaderState.Error,
                errorMessage = error.message ?: "Erreur inconnue",
                onRetry = { articles.retry() }
            )
        }

        else -> {
            if (articles.itemCount == 0) {
                EmptyListPlaceholder(
                    buttonText = "Réessayer",
                    onButtonClick = { articles.retry() }
                )
            }
            NewsListScreenPaging(articles = articles, onArticleClick = onArticleClick)
        }
    }
}
