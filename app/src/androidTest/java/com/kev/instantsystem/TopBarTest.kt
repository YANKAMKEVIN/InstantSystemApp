package com.kev.instantsystem

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.kev.instantsystem.ui.navigation.MainTopBar
import org.junit.Rule
import org.junit.Test

class TopBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun topBar_displaysTitle_andActions() {
        composeTestRule.setContent {
            MainTopBar()
        }

        composeTestRule.onNodeWithText("INSTANT SYSTEM").assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Profil").assertIsDisplayed()

        composeTestRule.onNodeWithText("S’abonner").assertIsDisplayed()
    }

    @Test
    fun topBar_buttons_areClickable() {
        composeTestRule.setContent {
            MainTopBar()
        }

        composeTestRule.onNodeWithContentDescription("Profil").performClick()
        composeTestRule.onNodeWithText("S’abonner").performClick()
    }
}
