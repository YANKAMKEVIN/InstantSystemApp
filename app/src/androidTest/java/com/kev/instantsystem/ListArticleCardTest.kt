package com.kev.instantsystem

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.domain.model.Source
import com.kev.instantsystem.ui.components.ListArticleCard
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ListArticleCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val article = Article(
        title = "Les marchés boursiers en hausse",
        description = "Description",
        content = "Contenu",
        url = "https://example.com",
        publishedAt = "2024-01-01T12:00:00Z",
        source = Source("Reuters", "reuters"),
        author = "Économie",
        urlToImage = null
    )

    @Test
    fun displaysArticleTitle() {
        composeTestRule.setContent {
            ListArticleCard(article = article, onClick = {})
        }

        composeTestRule.onNodeWithText(article.title).assertIsDisplayed()
    }

    @Test
    fun callsOnClickWhenTapped() {
        var clicked = false

        composeTestRule.setContent {
            ListArticleCard(article = article, onClick = { clicked = true })
        }

        composeTestRule.onNodeWithText(article.title).performClick()
        assert(clicked)
    }
}
