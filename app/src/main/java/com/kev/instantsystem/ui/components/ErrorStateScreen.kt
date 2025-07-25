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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ErrorStateScreen(
    modifier: Modifier = Modifier,
    title: String = "Une erreur s’est produite",
    message: String = "Vérifiez votre connexion ou réessayez plus tard.",
    buttonText: String = "Réessayer",
    onRetry: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = androidx.biometric.R.drawable.fingerprint_dialog_error),
                contentDescription = "Erreur",
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 24.dp)
                    .testTag("ErrorImage")
            )

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.testTag("ErrorTitle")
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.testTag("ErrorMessage")
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onRetry,
                modifier = Modifier.testTag("RetryButton")
            ) {
                Text(buttonText)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorStateScreenPreview() {
    ErrorStateScreen(
        title = "Erreur réseau",
        message = "Impossible de se connecter au serveur. Veuillez réessayer.",
        buttonText = "Réessayer",
        onRetry = {}
    )
}
