package com.kev.instantsystem.ui.home

sealed class ArticleCategory(val label: String, val query: String?) {
    data object Latest : ArticleCategory("Latest", null)
    data object Trump : ArticleCategory("Trump", "trump")
    data object Ukraine : ArticleCategory("Ukraine", "ukraine")
    data object Commerce : ArticleCategory("Commerce", "trade")
    data object Africa : ArticleCategory("Afrique", "africa")
    data object Israel : ArticleCategory("Israël", "israel")
}
