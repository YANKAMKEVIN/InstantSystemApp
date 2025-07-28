package com.kev.instantsystem.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.kev.instantsystem.R

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val iconRes: Int? = null,
    val defaultIcon: ImageVector? = null

) {
    init {
        require(iconRes != null || defaultIcon != null) {
            "BottomNavItem must have either iconRes or defaultIcon"
        }
    }

    data object Home :
        BottomNavItem(
            route = NavigationRoutes.HOME,
            label = "Headlines",
            iconRes = R.drawable.headlines_logo,

            )

    data object Search : BottomNavItem(
        route = NavigationRoutes.SEARCH,
        label = "Search",
        defaultIcon = Icons.Default.Search
    )

    data object Bookmarks :
        BottomNavItem(
            route = NavigationRoutes.BOOKMARK,
            label = "Lectures",
            defaultIcon = Icons.Default.Favorite
        )

    data object Discovery :
        BottomNavItem(
            route = NavigationRoutes.DISCOVERY,
            label = "Discovery",
            defaultIcon = Icons.Default.Email
        )

    data object Menu : BottomNavItem(
        route = NavigationRoutes.MENU,
        label = "Menu",
        defaultIcon = Icons.Default.Menu
    )

    companion object {
        val allItems = listOf(Home, Search, Bookmarks, Discovery, Menu)
    }
}