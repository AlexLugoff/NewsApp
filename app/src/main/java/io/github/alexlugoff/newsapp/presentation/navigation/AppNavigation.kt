package io.github.alexlugoff.newsapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.github.alexlugoff.newsapp.feature.details.presentation.NewsDetailsScreen
import io.github.alexlugoff.newsapp.feature.news.presentation.NewsListScreen
import io.github.alexlugoff.newsapp.feature.sources.presentation.SourceSelectionBottomSheetScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.NewsList.route
    ) {
        // Экран списка новостей
        composable(Screen.NewsList.route) {
            NewsListScreen(
                onNavigateToDetails = { url ->
                    navController.navigate(Screen.NewsDetails.createRoute(url))
                },
                openSourceSelectionSheet = { onDismiss ->
                    SourceSelectionBottomSheetScreen(onDismiss = onDismiss)
                }
            )
        }

        // Экран деталей новости
        composable(
            route = Screen.NewsDetails.route,
            arguments = listOf(
                navArgument("url") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val url = backStackEntry.arguments?.getString("url").orEmpty()
            NewsDetailsScreen(
                newsLink = url,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
