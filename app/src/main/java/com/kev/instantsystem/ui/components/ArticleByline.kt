package com.kev.instantsystem.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.ui.utils.articleByline
import com.kev.instantsystem.ui.utils.formatRelativeTime

/** "Le Monde · il y a 2 h" line shown on the article cards. */
@Composable
fun ArticleByline(article: Article, modifier: Modifier = Modifier) {
    val text = articleByline(article.source.name, formatRelativeTime(article.publishedAt))
    if (text.isNotEmpty()) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = modifier
        )
    }
}
