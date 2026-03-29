package com.example.newsapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.newsapp.feature.details.presentation.NewsDetailsScreen
import com.example.newsapp.feature.news.presentation.NewsListScreen
import com.example.newsapp.feature.sources.presentation.SourceSelectionBottomSheetScreen

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
