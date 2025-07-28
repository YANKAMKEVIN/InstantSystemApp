package com.kev.instantsystem

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.kev.instantsystem.ui.navigation.MainBottomBar
import com.kev.instantsystem.ui.navigation.NavigationRoutes
import org.junit.Rule
import org.junit.Test

class BottomBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun bottomBar_displaysAllItems_andRespondsToClick() {
        val selectedRoute = NavigationRoutes.HOME
        var selectedLabel: String? = null

        composeTestRule.setContent {
            MainBottomBar(
                currentRoute = selectedRoute,
                onItemSelected = { selectedLabel = it.label }
            )
        }

        composeTestRule.onNodeWithText("Home").assertIsDisplayed()
        composeTestRule.onNodeWithText("Search").assertIsDisplayed()
        composeTestRule.onNodeWithText("Headlines").assertIsDisplayed()
        composeTestRule.onNodeWithText("Lectures").assertIsDisplayed()
        composeTestRule.onNodeWithText("Menu").assertIsDisplayed()

        composeTestRule.onNodeWithText("Search").performClick()

        assert(selectedLabel == "Search")
    }
}
