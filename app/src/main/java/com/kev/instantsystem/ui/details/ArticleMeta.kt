package com.kev.instantsystem.ui.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kev.instantsystem.ui.utils.formatPublishedDate

@Composable
fun ArticleMeta(sourceName: String, publishedAt: String) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = sourceName,
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Publié " + formatPublishedDate(publishedAt),
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true, name = "ArticleMeta Preview")
@Composable
private fun PreviewArticleMeta() {
    ArticleMeta(
        sourceName = "Le Monde",
        publishedAt = "2025-07-24T12:00:00Z"
    )
}
