package com.kev.instantsystem

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.domain.model.Source
import com.kev.instantsystem.ui.components.MainArticleCard
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainArticleCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val article = Article(
        title = "LIVE - Situation en Ukraine",
        description = "Description",
        content = "Contenu",
        url = "https://example.com",
        publishedAt = "2024-01-01T12:00:00Z",
        source = Source("Le Monde", "1"),
        author = "Jean Dupont",
        urlToImage = "https://placehold.co/600x400"
    )

    @Test
    fun displaysTitleAndLiveBadge() {
        composeTestRule.setContent {
            MainArticleCard(article = article, onClick = {})
        }

        composeTestRule.onNodeWithText("LIVE").assertIsDisplayed()
        composeTestRule.onNodeWithText(article.title).assertIsDisplayed()
    }

    @Test
    fun callsOnClickWhenTapped() {
        var clicked = false

        composeTestRule.setContent {
            MainArticleCard(article = article, onClick = { clicked = true })
        }

        // Click anywhere in the card
        composeTestRule.onNodeWithText(article.title).performClick()
        assert(clicked)
    }

    @Test
    fun doesNotShowLiveBadgeIfTitleDoesNotContainLive() {
        val articleWithoutLive = article.copy(title = "Nouvelles du jour")
        composeTestRule.setContent {
            MainArticleCard(article = articleWithoutLive, onClick = {})
        }

        composeTestRule.onNodeWithText("LIVE").assertDoesNotExist()
        composeTestRule.onNodeWithText("Nouvelles du jour").assertIsDisplayed()
    }
}
