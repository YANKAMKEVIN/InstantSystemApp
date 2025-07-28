package com.kev.instantsystem.ui.headlines

sealed class ArticleCategory(val label: String, val category: String?) {
    data object Latest : ArticleCategory("Latest", null)
    data object Science : ArticleCategory("Science", "science")
    data object Sports : ArticleCategory("Sports", "sports")
    data object Technology : ArticleCategory("Technology", "technology")
    data object Health : ArticleCategory("Health", "health")
    data object Business : ArticleCategory("Business", "business")
}
