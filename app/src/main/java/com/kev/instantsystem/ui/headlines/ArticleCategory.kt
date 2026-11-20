package com.kev.instantsystem.ui.headlines

import androidx.annotation.StringRes
import com.kev.instantsystem.R

sealed class ArticleCategory(@StringRes val labelRes: Int, val category: String?) {
    data object Latest : ArticleCategory(R.string.category_latest, null)
    data object Science : ArticleCategory(R.string.category_science, "science")
    data object Sports : ArticleCategory(R.string.category_sports, "sports")
    data object Technology : ArticleCategory(R.string.category_technology, "technology")
    data object Health : ArticleCategory(R.string.category_health, "health")
    data object Business : ArticleCategory(R.string.category_business, "business")
}
