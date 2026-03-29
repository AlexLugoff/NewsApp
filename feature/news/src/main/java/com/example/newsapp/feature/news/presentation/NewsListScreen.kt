package com.example.newsapp.feature.news.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.newsapp.core.common.util.showLongToast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListScreen(
    viewModel: NewsListViewModel = hiltViewModel(),
    onNavigateToDetails: (String) -> Unit,
    openSourceSelectionSheet: @Composable (onDismiss: () -> Unit) -> Unit
) {
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val isRefreshing by viewModel._isRefreshing.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var isSheetOpen by remember { mutableStateOf(false) }
    val onNavigateToDetailsClick: (String) -> Unit = remember(onNavigateToDetails) {
        { link -> onNavigateToDetails(link) }
    }

    LaunchedEffect(viewModel.eventFlow) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is NewsListEvent.ShowErrorMessage -> {
                    context.showLongToast(event.message.asString(context))
                }
            }
        }
    }

    NewsListContent(
        uiState = uiState,
        isRefreshing = isRefreshing,
        isSheetOpen = isSheetOpen,
        onRefresh = viewModel::refreshNews,
        onToggleSheet = { isSheetOpen = it },
        onNavigateToDetails = onNavigateToDetailsClick,
        sourceSelectionSheet = {
            openSourceSelectionSheet({ isSheetOpen = false })
        }
    )
}
