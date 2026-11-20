package com.kev.instantsystem

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.kev.instantsystem.ui.navigation.MainTopBar
import org.junit.Rule
import org.junit.Test

class TopBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun topBar_displaysTitle() {
        composeTestRule.setContent {
            MainTopBar()
        }

        composeTestRule.onNodeWithText("INSTANT SYSTEM").assertIsDisplayed()
    }
}
