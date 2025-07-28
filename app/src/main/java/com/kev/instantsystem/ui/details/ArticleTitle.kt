package com.kev.instantsystem.ui.details

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ArticleTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 16.dp)
    )
}

@Preview(showBackground = true, name = "ArticleTitle Preview")
@Composable
private fun PreviewArticleTitle() {
    ArticleTitle(title = "Le climat mondial atteint un nouveau seuil critique")
}