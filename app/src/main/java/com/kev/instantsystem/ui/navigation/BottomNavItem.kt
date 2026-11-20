package com.kev.instantsystem.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.kev.instantsystem.R

sealed class BottomNavItem(
    val route: String,
    @StringRes val labelRes: Int,
    val iconRes: Int? = null,
    val defaultIcon: ImageVector? = null,
    /** Filled variant shown while the tab is selected, when the icon has one. */
    val selectedIconRes: Int? = null

) {
    init {
        require(iconRes != null || defaultIcon != null) {
            "BottomNavItem must have either iconRes or defaultIcon"
        }
    }

    data object Home :
        BottomNavItem(
            route = NavigationRoutes.HOME,
            labelRes = R.string.nav_home,
            iconRes = R.drawable.headlines_logo,

            )

    data object Search : BottomNavItem(
        route = NavigationRoutes.SEARCH,
        labelRes = R.string.nav_search,
        defaultIcon = Icons.Default.Search
    )

    data object Bookmarks :
        BottomNavItem(
            route = NavigationRoutes.BOOKMARK,
            labelRes = R.string.nav_bookmarks,
            iconRes = R.drawable.ic_bookmark_border,
            selectedIconRes = R.drawable.ic_bookmark
        )

    data object Headlines :
        BottomNavItem(
            route = NavigationRoutes.HEADLINES,
            labelRes = R.string.nav_headlines,
            iconRes = R.drawable.ic_newspaper
        )

    data object Menu : BottomNavItem(
        route = NavigationRoutes.MENU,
        labelRes = R.string.nav_menu,
        defaultIcon = Icons.Default.Menu
    )

    companion object {
        val allItems = listOf(Home, Search, Headlines, Bookmarks, Menu)
    }
}