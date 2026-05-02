package io.github.alexlugoff.newsapp.feature.sources.navigation

import androidx.compose.runtime.Composable
import io.github.alexlugoff.newsapp.feature.sources.presentation.SourceSelectionBottomSheetScreen

@Composable
fun SourceSelectionSheet(onDismiss: () -> Unit) {
    SourceSelectionBottomSheetScreen(onDismiss = onDismiss)
}
