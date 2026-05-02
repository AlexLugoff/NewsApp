package io.github.alexlugoff.newsapp.feature.details.navigation

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import io.github.alexlugoff.newsapp.core.common.util.DEEP_LINK_BASE_PATH
import io.github.alexlugoff.newsapp.feature.details.presentation.NewsDetailsScreen
import kotlinx.serialization.Serializable

@Serializable
data class NewsDetailsRoute(val url: String)

fun NavController.navigateToNewsDetails(url: String, navOptions: NavOptions? = null) {
    this.navigate(NewsDetailsRoute(url = Uri.encode(url)), navOptions)
}

fun NavGraphBuilder.newsDetailsScreen(
    onBackClick: () -> Unit
) {
    composable<NewsDetailsRoute>(
        deepLinks = listOf(
            navDeepLink<NewsDetailsRoute>(basePath = DEEP_LINK_BASE_PATH)
        )
    ) { backStackEntry ->
        val details: NewsDetailsRoute = backStackEntry.toRoute()
        NewsDetailsScreen(
            newsLink = Uri.decode(details.url),
            onBackClick = onBackClick
        )
    }
}
