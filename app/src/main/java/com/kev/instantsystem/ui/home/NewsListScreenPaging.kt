package com.kev.instantsystem.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.kev.instantsystem.ui.model.Article

@Composable
fun NewsListScreenPaging(
    articles: LazyPagingItems<Article>,
    onArticleClick: (Article) -> Unit = {}
) {
    LazyColumn(Modifier.background(Color.Black)) {
        items(articles.itemCount) { index ->
            val article = articles[index]
            article?.let {
                ArticleItem(article = it, index = index, onClick = onArticleClick)
            }
        }

        when (articles.loadState.append) {
            is LoadState.Loading -> {
                item {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }
            }

            is LoadState.Error -> {
                item {
                    Text("Erreur lors du chargement de plus d’articles")
                }
            }

            else -> Unit
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

