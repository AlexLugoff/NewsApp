package io.github.alexlugoff.newsapp.feature.details.presentation

import android.content.Intent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.alexlugoff.newsapp.core.common.util.INTENT_TYPE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailsScreen(
    newsLink: String,
    onBackClick: () -> Unit,
    viewModel: NewsDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(newsLink) {
        viewModel.loadNewsDetails(newsLink)
    }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is NewsDetailsEvent.GoToBrowser -> {
                    val intent = Intent(Intent.ACTION_VIEW, event.url.toUri())
                    context.startActivity(intent)
                }

                is NewsDetailsEvent.ShareNews -> {
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, event.url)
                        type = INTENT_TYPE
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                }
            }
        }
    }

    NewsDetailsContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onReadInBrowser = viewModel::goToBrowser,
        onShareClick = viewModel::shareNews
    )
}