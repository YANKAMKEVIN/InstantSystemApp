package com.kev.instantsystem.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.domain.model.Source
import com.kev.instantsystem.ui.theme.ISColors

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListArticleCard(
    modifier: Modifier = Modifier,
    article: Article,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = article.title,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                ArticleByline(article = article, modifier = Modifier.padding(top = 4.dp))
            }

            Spacer(modifier = Modifier.width(12.dp))

            ArticleThumbnail(article = article)
        }

        Spacer(modifier = Modifier.height(12.dp))

        HorizontalDivider(thickness = 0.5.dp, color = Color.DarkGray)
    }
}

/** Same source, same colour: the colour is picked from the source name. */
internal fun sourceColorIndex(sourceName: String, paletteSize: Int): Int =
    Math.floorMod(sourceName.trim().lowercase().hashCode(), paletteSize)

internal fun sourceInitial(sourceName: String): String =
    sourceName.trim().firstOrNull { it.isLetterOrDigit() }?.uppercase() ?: "?"

@Composable
private fun ArticleThumbnail(article: Article) {
    val shape = RoundedCornerShape(8.dp)
    val initialBackgrounds = MaterialTheme.ISColors.let {
        listOf(
            it.Brand._700,
            it.Success._600,
            it.Warning._700,
            it.Danger._600,
            it.AccentPink._700,
            it.AccentYellow._600
        )
    }
    if (article.urlToImage != null) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(article.urlToImage)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .size(72.dp)
                .clip(shape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .semantics { testTag = "ArticleImage" },
            contentScale = ContentScale.Crop
        )
    } else {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(shape)
                .background(
                    initialBackgrounds[sourceColorIndex(article.source.name, initialBackgrounds.size)]
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = sourceInitial(article.source.name),
                fontWeight = FontWeight.Black,
                fontSize = 24.sp,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true, name = "ListArticleCard Preview")
@Composable
private fun PreviewListArticleCard() {
    ListArticleCard(
        article = Article(
            title = "Intelligence artificielle : quelles limites pour demain ?",
            description = null,
            content = null,
            url = "https://example.com/ai",
            publishedAt = "2025-07-24",
            source = Source("Tech News", "tech-news"),
            author = "Alice Dupont",
            urlToImage = null
        ),
        onClick = {}
    )
}
