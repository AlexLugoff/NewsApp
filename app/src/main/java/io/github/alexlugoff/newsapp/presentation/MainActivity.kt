package io.github.alexlugoff.newsapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import io.github.alexlugoff.newsapp.core.domain.usecase.ClearOldNewsUseCase
import io.github.alexlugoff.newsapp.core.ui.theme.NewsTheme
import io.github.alexlugoff.newsapp.presentation.navigation.AppNavigation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var clearOldNewsUseCase: ClearOldNewsUseCase

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition {
            viewModel.isLoading.value
        }

        enableEdgeToEdge()
        setContent {
            LaunchedEffect(Unit) {
                clearOldNewsUseCase()
            }

            NewsTheme {
                AppNavigation()
            }
        }
    }

}