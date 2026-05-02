package io.github.alexlugoff.newsapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import io.github.alexlugoff.newsapp.feature.details.navigation.newsDetailsScreen
import io.github.alexlugoff.newsapp.feature.news.navigation.NewsListRoute
import io.github.alexlugoff.newsapp.feature.news.navigation.newsListScreen
import io.github.alexlugoff.newsapp.feature.sources.navigation.SourceSelectionSheet

@Composable
fun AppNavigation(
    appState: NewsAppState = rememberNewsAppState()
) {
    NavHost(
        navController = appState.navController,
        startDestination = NewsListRoute
    ) {
        newsListScreen(
            onNavigateToDetails = { url ->
                appState.navigateToDetails(url)
            },
            openSourceSelectionSheet = { onDismiss ->
                SourceSelectionSheet(onDismiss = onDismiss)
            }
        )

        newsDetailsScreen(
            onBackClick = { appState.onBackClick() }
        )
    }
}
