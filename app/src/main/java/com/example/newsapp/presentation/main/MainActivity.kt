package com.example.newsapp.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import com.example.composetraining.ui.theme.NewsTheme
import com.example.newsapp.domain.usecases.ClearOldNewsUseCase
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var clearOldNewsUseCase: ClearOldNewsUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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