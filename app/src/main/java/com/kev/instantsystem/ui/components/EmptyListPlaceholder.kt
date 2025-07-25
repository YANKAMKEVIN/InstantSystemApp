package com.kev.instantsystem.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EmptyListPlaceholder(
    modifier: Modifier = Modifier,
    title: String = "Aucun résultat",
    subtitle: String = "Essayez de modifier vos critères de recherche.",
    emoji: String? = "📰",
    imageResId: Int? = null,
    buttonText: String? = null,
    onButtonClick: (() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            imageResId?.let {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .padding(bottom = 16.dp)
                        .testTag("EmptyListImage")
                )
            } ?: emoji?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.displayMedium,
                    fontSize = 48.sp,
                    modifier = Modifier.testTag("EmptyListEmoji")
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.testTag("EmptyListSubtitle")
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (!buttonText.isNullOrBlank() && onButtonClick != null) {
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = onButtonClick, modifier = Modifier.testTag("EmptyListButton")) {
                    Text(buttonText)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyListPlaceholderPreview() {
    EmptyListPlaceholder(
        title = "Aucune actualité",
        subtitle = "Essayez avec un autre mot-clé",
        emoji = "📰",
        buttonText = "Réessayer",
        onButtonClick = {}
    )
}