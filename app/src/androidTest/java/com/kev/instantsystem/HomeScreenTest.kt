package com.kev.instantsystem

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.domain.model.Source
import com.kev.instantsystem.ui.components.ListArticleCard
import com.kev.instantsystem.ui.components.MainArticleCard
import com.kev.instantsystem.ui.details.ArticleDetailsScreen
import com.kev.instantsystem.ui.placeholder.ScreenPlaceholder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun featuredArticleCard_displaysTitleAndLiveBadge_whenLive() {
        val article = Article(
            title = "LIVE - Événement majeur en cours",
            source = Source("1", "Le Monde"),
            author = "Auteur",
            description = "Ceci est une description",
            url = "https://example.com",
            urlToImage = null,
            publishedAt = "2024-06-01T10:00:00Z",
            content = "Contenu de l’article"
        )

        composeTestRule.setContent {
            MainArticleCard(article = article, onClick = {})
        }

        composeTestRule.onNodeWithText("LIVE").assertIsDisplayed()
        composeTestRule.onNodeWithText(article.title).assertIsDisplayed()
    }

    @Test
    fun secondaryArticleCard_displaysTitle_andRespondsToClick() {
        val article = Article(
            title = "Titre secondaire",
            source = Source("2", "Source X"),
            author = null,
            description = null,
            url = "",
            urlToImage = null,
            publishedAt = "2024-06-01T10:00:00Z",
            content = null
        )

        var clicked = false

        composeTestRule.setContent {
            ListArticleCard(article = article, onClick = { clicked = true })
        }

        composeTestRule.onNodeWithText("Titre secondaire").performClick()
        assert(clicked)
    }

    @Test
    fun articleDetails_displaysDescription_andButton() {
        val article = Article(
            title = "Titre de l’article",
            source = Source(null, "Source Y"),
            author = null,
            description = "Une description complète.",
            url = "https://example.com",
            urlToImage = null,
            publishedAt = "2024-06-01T10:00:00Z",
            content = "Contenu de l’article"
        )

        composeTestRule.setContent {
            ArticleDetailsScreen(article = article)
        }

        composeTestRule.onNodeWithText("Une description complète.").assertIsDisplayed()
        composeTestRule.onNodeWithText("Lire l’article complet").assertIsDisplayed()
    }

    @Test
    fun placeholder_displaysTitle_andEmoji() {
        composeTestRule.setContent {
            ScreenPlaceholder(title = "Journal", emoji = "📰")
        }

        composeTestRule.onNodeWithText("📰").assertIsDisplayed()
        composeTestRule.onNodeWithText("Journal").assertIsDisplayed()
    }
}
