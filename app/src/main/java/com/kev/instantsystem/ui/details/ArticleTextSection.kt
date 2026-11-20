package com.kev.instantsystem.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** NewsAPI cuts `content` and appends "[+1234 chars]": that marker means the text is truncated. */
private val truncationMarker = Regex("""\s*\[\+\d+\s*chars]\s*$""")

/** The article content without NewsAPI's "[+N chars]" suffix, or null when there is nothing to show. */
fun cleanArticleContent(content: String?): String? =
    content?.replace(truncationMarker, "")?.trim()?.takeIf { it.isNotEmpty() }

fun isTruncatedContent(content: String?): Boolean =
    content != null && (truncationMarker.containsMatchIn(content) || content.trimEnd().endsWith("…"))

@Composable
fun ArticleTextSection(text: String?, style: TextStyle = MaterialTheme.typography.bodyMedium) {
    text?.let {
        Text(
            text = it,
            style = style,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

/** Lead paragraph (the NewsAPI description), set apart from the body. */
@Composable
fun ArticleLead(text: String?) {
    ArticleTextSection(
        text = text,
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            lineHeight = 24.sp
        )
    )
}

/** Article body; a truncated body fades out towards the "read the full article" button. */
@Composable
fun ArticleBody(content: String?) {
    val text = cleanArticleContent(content) ?: return
    Box {
        ArticleTextSection(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 28.sp)
        )
        if (isTruncatedContent(content)) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, MaterialTheme.colorScheme.background)
                        )
                    )
            )
        }
    }
}

@Preview(showBackground = true, name = "ArticleTextSection Preview")
@Composable
private fun PreviewArticleTextSection() {
    ArticleBody(
        content = "Selon les scientifiques, les températures globales ont franchi un seuil critique cette année… [+1234 chars]"
    )
}
