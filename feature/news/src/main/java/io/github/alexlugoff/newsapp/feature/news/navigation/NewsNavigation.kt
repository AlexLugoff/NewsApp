package io.github.alexlugoff.newsapp.feature.news.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import io.github.alexlugoff.newsapp.feature.news.presentation.NewsListScreen
import kotlinx.serialization.Serializable

@Serializable
object NewsListRoute

@Suppress("unused")
fun NavController.navigateToNewsList(navOptions: NavOptions? = null) {
    this.navigate(NewsListRoute, navOptions)
}

fun NavGraphBuilder.newsListScreen(
    onNavigateToDetails: (String) -> Unit,
    openSourceSelectionSheet: @Composable (onDismiss: () -> Unit) -> Unit
) {
    composable<NewsListRoute> {
        NewsListScreen(
            onNavigateToDetails = onNavigateToDetails,
            openSourceSelectionSheet = openSourceSelectionSheet
        )
    }
}
