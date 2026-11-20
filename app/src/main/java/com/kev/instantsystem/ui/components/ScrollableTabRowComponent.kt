package com.kev.instantsystem.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kev.instantsystem.ui.theme.ISColors
import com.kev.instantsystem.ui.theme.ISTypography
import com.kev.instantsystem.ui.utils.SpacerVertical
import kotlinx.coroutines.launch

/**
 * A custom Composable for creating a tabbed interface whose content can be swiped between tabs.
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
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val scope = rememberCoroutineScope()
    val selectedTabIndex = pagerState.currentPage

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
                    onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                ) {
                    Text(
                        text = tabTitle,
                        color = Color.White,
                        style = if (selected) MaterialTheme.ISTypography.body.defaultMedium else MaterialTheme.ISTypography.body.medium
                    )
                }
            }
        }
        SpacerVertical(8.dp)
        // Swiping the content changes the tab, and tapping a tab scrolls the content.
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            key = { it }
        ) { page ->
            contentScreens.getOrNull(page)?.invoke()
        }
    }
}