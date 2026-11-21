package com.kev.instantsystem.data

import com.kev.instantsystem.data.local.headlinesCacheKey
import com.kev.instantsystem.data.local.toDomain
import com.kev.instantsystem.data.local.toHeadlineEntity
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.domain.model.Source
import org.junit.Assert.assertEquals
import org.junit.Test

class HeadlineEntitiesTest {

    @Test
    fun `an article comes back unchanged from the cache`() {
        val article = Article(
            source = Source(id = null, name = "Le Monde"),
            author = "Jean",
            title = "Title",
            description = "Desc",
            url = "https://a",
            urlToImage = "https://a/img.jpg",
            publishedAt = "2025-07-24T12:00:00Z",
            content = "Content"
        )

        val entity = article.toHeadlineEntity(cacheKey = "us:", position = 3)

        assertEquals(3, entity.position)
        assertEquals(article, entity.toDomain())
    }

    @Test
    fun `cache keys separate countries and categories`() {
        assertEquals("us:", headlinesCacheKey("us", null))
        assertEquals("us:sports", headlinesCacheKey("us", "sports"))
    }
}
