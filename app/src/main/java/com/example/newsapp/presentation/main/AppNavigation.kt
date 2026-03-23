package com.example.newsapp.presentation.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.newsapp.presentation.news.NewsListScreen
import com.example.newsapp.presentation.news_details.NewsDetailsScreen

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