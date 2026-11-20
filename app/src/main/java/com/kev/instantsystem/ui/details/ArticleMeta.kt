package com.kev.instantsystem.ui.details

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kev.instantsystem.ui.utils.articleByline
import com.kev.instantsystem.ui.utils.formatPublishedDate

/** "Le Monde · Aujourd’hui à 14:05" on a single line. */
@Composable
fun ArticleMeta(sourceName: String, publishedAt: String) {
    Text(
        text = articleByline(sourceName, formatPublishedDate(publishedAt)),
        style = MaterialTheme.typography.labelMedium,
        color = Color.Gray,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
    )
}

@Preview(showBackground = true, name = "ArticleMeta Preview")
@Composable
private fun PreviewArticleMeta() {
    ArticleMeta(
        sourceName = "Le Monde",
        publishedAt = "2025-07-24T12:00:00Z"
    )
}
