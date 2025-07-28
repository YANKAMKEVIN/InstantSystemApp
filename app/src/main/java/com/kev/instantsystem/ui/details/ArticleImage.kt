package com.kev.instantsystem.ui.details

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ArticleImage(imageUrl: String) {
    AsyncImage(
        model = imageUrl,
        contentDescription = "Illustration de l’article",
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)),
        contentScale = ContentScale.Crop
    )
}

@Preview(showBackground = true, name = "ArticleImage Preview")
@Composable
private fun PreviewArticleImage() {
    ArticleImage(imageUrl = "https://placehold.co/600x400")
}