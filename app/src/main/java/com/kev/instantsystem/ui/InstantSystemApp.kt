package com.kev.instantsystem.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kev.instantsystem.ui.discovery.DiscoveryRoute
import com.kev.instantsystem.ui.home.HomeRoute
import com.kev.instantsystem.ui.menu.MenuScreen
import com.kev.instantsystem.ui.navigation.BottomNavItem
import com.kev.instantsystem.ui.navigation.MainBottomBar
import com.kev.instantsystem.ui.navigation.MainTopBar
import com.kev.instantsystem.ui.placeholder.ScreenPlaceholder
import com.kev.instantsystem.ui.search.SearchRoute

@Composable
fun InstantSystemApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isDetailScreenVisible = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            if (!isDetailScreenVisible.value) {
                MainTopBar()
            }
        },
        content = { paddingValue ->
            ISNavHost(
                navController = navController,
                modifier = Modifier.padding(paddingValue),
                onDetailVisibilityChanged = { isDetailScreenVisible.value = it }
            )

        },
        bottomBar = {
            MainBottomBar(
                currentRoute = currentRoute,
                onItemSelected = { navigateTo(navController, it.route) }

            )
        }
    )
}

fun navigateTo(navController: NavHostController, route: String) {
    navController.navigate(route) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
private fun ISNavHost(
    navController: NavHostController,
    modifier: Modifier,
    onDetailVisibilityChanged: (Boolean) -> Unit
) {

    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = BottomNavItem.Home.route,
    ) {
        composable(BottomNavItem.Home.route) {
            HomeRoute(
                onDetailVisibilityChanged = onDetailVisibilityChanged
            )
        }
        composable(BottomNavItem.Search.route) {
            SearchRoute()
        }
        composable(BottomNavItem.Discovery.route) {
            DiscoveryRoute()
        }
        composable(BottomNavItem.Bookmarks.route) {
            BookmarksScreen()
        }
        composable(BottomNavItem.Menu.route) {
            MenuScreen()
        }
    }
}

@Composable
private fun BookmarksScreen() {
    ScreenPlaceholder(title = "Bookmarks", emoji = "🔖")
}
