package com.kev.instantsystem.ui.components

import androidx.compose.ui.res.stringResource
import com.kev.instantsystem.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.kev.instantsystem.domain.model.Article

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListScreenPaging(
    articles: LazyPagingItems<Article>,
    onArticleClick: (Article) -> Unit = {},
    onArticleLongClick: ((Article) -> Unit)? = null
) {
    PullToRefreshBox(
        isRefreshing = articles.loadState.refresh is LoadState.Loading,
        onRefresh = { articles.refresh() },
        modifier = Modifier.fillMaxSize()
    ) {
        ArticleLazyColumn(articles, onArticleClick, onArticleLongClick)
    }
}

@Composable
private fun ArticleLazyColumn(
    articles: LazyPagingItems<Article>,
    onArticleClick: (Article) -> Unit,
    onArticleLongClick: ((Article) -> Unit)?
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(articles.itemCount) { index ->
            val article = articles[index]
            article?.let {
                ArticleItem(
                    article = it,
                    index = index,
                    onClick = onArticleClick,
                    onLongClick = onArticleLongClick
                )
            }
        }

        when (articles.loadState.append) {
            is LoadState.Loading -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            is LoadState.Error -> {
                item {
                    AppendErrorFooter(onRetry = articles::retry)
                }
            }

            else -> Unit
        }
    }
}

/** Shown under the list when the next page failed, with a way to load that page again. */
@Composable
internal fun AppendErrorFooter(onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.append_error),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        OutlinedButton(onClick = onRetry) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
private fun ArticleItem(
    article: Article,
    index: Int,
    onClick: (Article) -> Unit,
    onLongClick: ((Article) -> Unit)?
) {
    val longClick = onLongClick?.let { { it(article) } }
    if (index == 0) {
        MainArticleCard(article = article, onLongClick = longClick, onClick = { onClick(article) })
    } else {
        ListArticleCard(article = article, onClick = { onClick(article) }, onLongClick = longClick)
    }
}

