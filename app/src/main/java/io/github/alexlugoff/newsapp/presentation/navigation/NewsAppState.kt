package io.github.alexlugoff.newsapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.github.alexlugoff.newsapp.feature.details.navigation.navigateToNewsDetails

@Composable
fun rememberNewsAppState(
    navController: NavHostController = rememberNavController(),
): NewsAppState {
    return remember(navController) {
        NewsAppState(navController)
    }
}

@Stable
class NewsAppState(
    val navController: NavHostController,
) {
    fun navigateToDetails(url: String) {
        navController.navigateToNewsDetails(url)
    }

    fun onBackClick() {
        navController.popBackStack()
    }
}
