package com.kev.instantsystem

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kev.instantsystem.ui.menu.MenuScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MenuScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysMenuTitle() {
        composeTestRule.setContent {
            MenuScreen()
        }

        composeTestRule.onNodeWithText("Menu").assertIsDisplayed()
    }

    @Test
    fun displaysAllMenuOptions() {
        val expectedOptions = listOf(
            "Your account",
            "Settings",
            "About this app",
            "Rate us",
            "Share the app",
            "Send feedback"
        )

        composeTestRule.setContent {
            MenuScreen()
        }

        expectedOptions.forEach { label ->
            composeTestRule.onNodeWithText(label).assertIsDisplayed()
        }
    }
}
