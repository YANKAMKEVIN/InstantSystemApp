package com.kev.instantsystem.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kev.instantsystem.R

/** Topic chips and recent searches shown before the user has typed a query. */
@Composable
fun SearchSuggestions(
    recentSearches: List<String>,
    onSuggestionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SuggestionGroup(
            title = stringResource(R.string.search_topics_title),
            suggestions = stringArrayResource(R.array.search_topics).toList(),
            onSuggestionSelected = onSuggestionSelected
        )
        if (recentSearches.isNotEmpty()) {
            SuggestionGroup(
                title = stringResource(R.string.search_recent_title),
                suggestions = recentSearches,
                onSuggestionSelected = onSuggestionSelected
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SuggestionGroup(
    title: String,
    suggestions: List<String>,
    onSuggestionSelected: (String) -> Unit
) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        suggestions.forEach { suggestion ->
            SuggestionChip(
                onClick = { onSuggestionSelected(suggestion) },
                label = { Text(suggestion) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSearchSuggestions() {
    SearchSuggestions(recentSearches = listOf("bitcoin", "Tour de France"), onSuggestionSelected = {})
}
