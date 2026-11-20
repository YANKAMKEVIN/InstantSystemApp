package com.kev.instantsystem.ui.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.domain.model.Source

@Composable
fun ArticleDetailsScreen(
    article: Article,
    isBookmarked: Boolean = false,
    onToggleBookmark: () -> Unit = {},
    onReadMoreClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ArticleTitle(article.title)

        article.urlToImage?.let { imageUrl ->
            ArticleImage(imageUrl)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.weight(1f)) {
                ArticleMeta(
                    sourceName = article.source.name,
                    publishedAt = article.publishedAt
                )
            }
            BookmarkButton(
                isBookmarked = isBookmarked,
                onClick = onToggleBookmark,
                modifier = Modifier.padding(end = 4.dp)
            )
        }

        ArticleTextSection(text = article.description)
        ArticleTextSection(text = article.content)

        ReadMoreButton(onReadMoreClick)
    }
}


@Preview(showBackground = true, name = "ArticleDetailsScreen Preview")
@Composable
private fun PreviewArticleDetailsScreen() {
    ArticleDetailsScreen(
        article = Article(
            title = "Le climat mondial atteint un nouveau seuil critique",
            description = "Une étude révèle l'accélération du changement climatique.",
            content = "Selon les scientifiques, les températures globales ont franchi un seuil critique cette année...",
            url = "https://example.com/climat",
            publishedAt = "2025-07-24T12:00:00Z",
            source = Source(name = "Le Monde", id = "lemonde"),
            author = "Jean Dupont",
            urlToImage = "https://placehold.co/600x400"
        ),
        onReadMoreClick = {}
    )
}
