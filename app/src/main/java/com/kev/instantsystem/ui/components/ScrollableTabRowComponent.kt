package com.kev.instantsystem.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import com.kev.instantsystem.ui.theme.ISColors
import com.kev.instantsystem.ui.theme.ISTypography
import com.kev.instantsystem.ui.utils.SpacerVertical

/**
 * A custom Composable for creating a tabbed interface.
 *
 * @param tabs List of tab titles.
 * @param contentScreens List of Composable functions representing content screens for each tab.
 * @param modifier Modifier for the parent layout.
 * @param containerColor Background color for the tab row container.
 * @param contentColor Color for the text content of the tabs.
 * @param indicatorColor Color for the indicator line.
 */
@Composable
fun ScrollableTabRowComponent(
    tabs: List<String>,
    contentScreens: List<@Composable () -> Unit>,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = MaterialTheme.colorScheme.background,
    indicatorColor: Color = MaterialTheme.ISColors.Brand._300
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val textWidths = remember { mutableStateListOf<Int>() }

    Column(modifier = modifier.fillMaxSize()) {
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = containerColor,
            contentColor = contentColor,
            indicator =
                @Composable {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier
                            .tabIndicatorOffset(it[selectedTabIndex]),
                        color = indicatorColor
                    )
                },
            divider = {}
        ) {
            tabs.forEachIndexed { index, tabTitle ->
                val selected = index == selectedTabIndex
                Tab(
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp),
                    selected = selected,
                    onClick = { selectedTabIndex = index },
                ) {
                    Text(
                        text = tabTitle,
                        color = Color.White,
                        style = if (selectedTabIndex == index) MaterialTheme.ISTypography.body.defaultMedium else MaterialTheme.ISTypography.body.medium,
                        modifier = Modifier.onGloballyPositioned {
                            val width = it.size.width
                            if (index >= textWidths.size) {
                                textWidths.add(width)
                            } else {
                                textWidths[index] = width
                            }
                        }
                    )
                }
            }
        }
        SpacerVertical(8.dp)
        contentScreens.getOrNull(selectedTabIndex)?.invoke() ?: Log.e(
            "TabRow",
            "No content for tab index $selectedTabIndex"
        )
    }
}