package com.kev.instantsystem

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.kev.instantsystem.ui.components.EmptyListPlaceholder
import org.junit.Rule
import org.junit.Test

class EmptyListPlaceholderTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysTitleSubtitleAndEmoji() {
        composeTestRule.setContent {
            EmptyListPlaceholder(
                title = "Aucun élément",
                subtitle = "Essayez autre chose",
                emoji = "🔍",
                buttonText = null,
                onButtonClick = null
            )
        }

        composeTestRule.onNodeWithText("Aucun élément").assertIsDisplayed()
        composeTestRule.onNodeWithText("Essayez autre chose").assertIsDisplayed()
        composeTestRule.onNodeWithText("🔍").assertIsDisplayed()
    }

    @Test
    fun displaysButton_whenTextAndCallbackProvided() {
        composeTestRule.setContent {
            EmptyListPlaceholder(
                title = "Rien ici",
                subtitle = "Changez les filtres",
                buttonText = "Réessayer",
                onButtonClick = {}
            )
        }

        composeTestRule.onNodeWithText("Réessayer").assertIsDisplayed()
    }

    @Test
    fun callsOnClick_whenButtonClicked() {
        var clicked = false

        composeTestRule.setContent {
            EmptyListPlaceholder(
                title = "Oops",
                subtitle = "Aucun résultat",
                buttonText = "Nouvelle recherche",
                onButtonClick = { clicked = true }
            )
        }

        composeTestRule.onNodeWithText("Nouvelle recherche").performClick()
        assert(clicked)
    }
}
