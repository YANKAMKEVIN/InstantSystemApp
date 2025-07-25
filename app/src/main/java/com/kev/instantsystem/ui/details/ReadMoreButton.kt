package com.kev.instantsystem.ui.details

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ReadMoreButton(onClick: () -> Unit) {
    Spacer(modifier = Modifier.height(24.dp))
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        Text("Lire l’article complet")
    }
    Spacer(modifier = Modifier.height(32.dp))
}

@Preview(showBackground = true, name = "ReadMoreButton Preview")
@Composable
private fun PreviewReadMoreButton() {
    ReadMoreButton(onClick = {})
}