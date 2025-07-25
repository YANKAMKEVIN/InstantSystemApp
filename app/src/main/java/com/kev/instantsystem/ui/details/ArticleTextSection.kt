package com.kev.instantsystem.ui.details

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ArticleTextSection(text: String?) {
    text?.let {
        Text(
            text = it,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Preview(showBackground = true, name = "ArticleTextSection Preview")
@Composable
private fun PreviewArticleTextSection() {
    ArticleTextSection(
        text = "Selon les scientifiques, les températures globales ont franchi un seuil critique cette année..."
    )
}