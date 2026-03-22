package com.example.newsapp.presentation.news

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListScreen(
    viewModel: NewsListViewModel = hiltViewModel(), onNavigateToDetails: (String) -> Unit
) {
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val isRefreshing by viewModel._isRefreshing.collectAsStateWithLifecycle()

    var isSheetOpen by remember { mutableStateOf(false) }
    val onNavigateToDetailsClick: (String) -> Unit = remember(onNavigateToDetails) {
        { link -> onNavigateToDetails(link) }
    }

    NewsListContent(
        uiState = uiState,
        isRefreshing = isRefreshing,
        isSheetOpen = isSheetOpen,
        onRefresh = viewModel::refreshNews,
        onToggleSheet = { isSheetOpen = it },
        onNavigateToDetails = onNavigateToDetailsClick
    )
}

