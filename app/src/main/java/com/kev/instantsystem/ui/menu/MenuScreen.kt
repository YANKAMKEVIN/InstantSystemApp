package com.kev.instantsystem.ui.menu

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kev.instantsystem.BuildConfig
import com.kev.instantsystem.R
import com.kev.instantsystem.ui.utils.openCustomTab

private const val REPOSITORY_URL = "https://github.com/YANKAMKEVIN/InstantSystemApp"
private const val NEWS_API_URL = "https://newsapi.org"

@Composable
fun MenuScreen() {
    val context = LocalContext.current
    var showAbout by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = stringResource(R.string.menu_title),
            style = MaterialTheme.typography.headlineLarge
        )

        MenuOption(
            icon = Icons.Default.Share,
            title = stringResource(R.string.menu_share_app),
            onClick = { shareApp(context) }
        )
        MenuOption(
            icon = Icons.Default.Info,
            title = stringResource(R.string.menu_about),
            onClick = { showAbout = true }
        )
    }

    if (showAbout) {
        AboutDialog(
            onOpenUrl = { openCustomTab(context, it) },
            onDismiss = { showAbout = false }
        )
    }
}

private fun shareApp(context: Context) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, context.getString(R.string.menu_share_text, REPOSITORY_URL))
    }
    context.startActivity(Intent.createChooser(intent, context.getString(R.string.menu_share_app)))
}

@Composable
private fun AboutDialog(
    onOpenUrl: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.app_name)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(stringResource(R.string.about_version, BuildConfig.VERSION_NAME))
                Text(stringResource(R.string.about_news_credit))
                TextButton(onClick = { onOpenUrl(NEWS_API_URL) }) {
                    Text(stringResource(R.string.about_news_api_link))
                }
                TextButton(onClick = { onOpenUrl(REPOSITORY_URL) }) {
                    Text(stringResource(R.string.about_source_code))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.about_close))
            }
        }
    )
}

@Composable
private fun MenuOption(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewMenuScreen() {
    MenuScreen()
}

@Preview(showBackground = true)
@Composable
private fun PreviewMenuOption() {
    MenuOption(icon = Icons.Default.Settings, title = "Paramètres")
}
