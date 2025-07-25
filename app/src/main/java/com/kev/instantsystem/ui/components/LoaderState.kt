package com.kev.instantsystem.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

enum class LoaderState {
    Loading, Success, Error
}

@Composable
fun LoaderScreen(
    state: LoaderState,
    onRetry: () -> Unit = {},
    errorMessage: String = "Une erreur est survenue",
    content: @Composable () -> Unit = {}
) {
    when (state) {
        LoaderState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.testTag("LoaderIndicator")
                )
            }
        }

        LoaderState.Success -> {
            content()
        }

        LoaderState.Error -> {
            ErrorStateScreen(
                message = errorMessage,
                onRetry = onRetry
            )
        }
    }
}

@Preview(showBackground = true, name = "LoaderScreen - Loading")
@Composable
private fun PreviewLoaderScreenLoading() {
    LoaderScreen(state = LoaderState.Loading)
}

@Preview(showBackground = true, name = "LoaderScreen - Error")
@Composable
private fun PreviewLoaderScreenError() {
    LoaderScreen(state = LoaderState.Error)
}

@Preview(showBackground = true, name = "LoaderScreen - Success")
@Composable
private fun PreviewLoaderScreenSuccess() {
    LoaderScreen(state = LoaderState.Success) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Contenu chargé avec succès !")
        }
    }
}
