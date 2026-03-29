package com.example.newsapp.feature.news.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.example.newsapp.core.ui.component.EmptyState
import com.example.newsapp.core.ui.component.ErrorMessage
import com.example.newsapp.core.ui.component.LoadingIndicator
import com.example.newsapp.core.ui.component.NewsAppBar
import com.example.newsapp.feature.news.R
import com.example.newsapp.feature.news.presentation.components.NewsList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListContent(
    uiState: NewsListViewState,
    isRefreshing: Boolean,
    isSheetOpen: Boolean,
    onRefresh: () -> Unit,
    onToggleSheet: (Boolean) -> Unit,
    onNavigateToDetails: (String) -> Unit,
    sourceSelectionSheet: @Composable () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val lazyListState = rememberLazyListState()
    val pullToRefreshState = rememberPullToRefreshState()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            NewsAppBar(
                title = stringResource(R.string.app_name),
                scrollBehavior = scrollBehavior,
                actions = {
                    IconButton(onClick = { onToggleSheet(true) }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = stringResource(R.string.select_sources)
                        )
                    }
                }
            )
        }
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            state = pullToRefreshState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            when (uiState) {
                is NewsListViewState.Loading -> LoadingIndicator()

                is NewsListViewState.Success -> {
                    if (uiState.news.isEmpty()) {
                        EmptyState()
                    } else {
                        NewsList(uiState.news, onNavigateToDetails, lazyListState)
                    }
                }

                is NewsListViewState.Error -> {
                    ErrorMessage(uiState.message.asString())
                }
            }
        }
    }

    if (isSheetOpen) {
        sourceSelectionSheet()
    }
}
