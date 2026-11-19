package com.kev.instantsystem.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.kev.instantsystem.domain.model.Article

@Composable
fun NewsListScreenPaging(
    articles: LazyPagingItems<Article>,
    onArticleClick: (Article) -> Unit = {}
) {
    LazyColumn {
        items(articles.itemCount) { index ->
            val article = articles[index]
            article?.let {
                ArticleItem(article = it, index = index, onClick = onArticleClick)
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
            text = "Impossible de charger plus d’articles.",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        OutlinedButton(onClick = onRetry) {
            Text("Réessayer")
        }
    }
}

@Composable
private fun ArticleItem(article: Article, index: Int, onClick: (Article) -> Unit) {
    if (index == 0) {
        MainArticleCard(article = article, onClick = { onClick(article) })
    } else {
        ListArticleCard(article = article, onClick = { onClick(article) })
    }
}

