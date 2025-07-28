package com.kev.instantsystem

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.kev.instantsystem.ui.components.ErrorStateScreen
import org.junit.Rule
import org.junit.Test

class ErrorStateScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysTitleMessageAndRetryButton() {
        composeTestRule.setContent {
            ErrorStateScreen(
                title = "Erreur critique",
                message = "Impossible de charger les données.",
                buttonText = "Réessayer",
                onRetry = {}
            )
        }

        composeTestRule.onNodeWithText("Erreur critique").assertIsDisplayed()
        composeTestRule.onNodeWithText("Impossible de charger les données.").assertIsDisplayed()
        composeTestRule.onNodeWithText("Réessayer").assertIsDisplayed()
    }

    @Test
    fun callsOnRetry_whenRetryButtonClicked() {
        var retryCalled = false

        composeTestRule.setContent {
            ErrorStateScreen(
                onRetry = { retryCalled = true }
            )
        }

        composeTestRule.onNodeWithText("Réessayer").performClick()

        assert(retryCalled)
    }
}
