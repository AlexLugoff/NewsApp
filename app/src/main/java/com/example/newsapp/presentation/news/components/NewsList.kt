package com.example.newsapp.presentation.news.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.newsapp.core.model.NewsItem
import my.nanihadesuka.compose.LazyColumnScrollbar
import my.nanihadesuka.compose.ScrollbarSettings

@Composable
fun NewsList(
    news: List<NewsItem>,
    onNavigateToDetails: (String) -> Unit,
    state: LazyListState,
    modifier: Modifier = Modifier
) {
    LazyColumnScrollbar(
        state = state,
        settings = ScrollbarSettings.Default.copy(
            thumbUnselectedColor = MaterialTheme.colorScheme.primary, scrollbarPadding = 4.dp
        )
    ) {
        LazyColumn(
            state = state,
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(
                items = news, key = { it.link }) { item ->
                NewsItemCard(
                    news = item, onClick = { onNavigateToDetails(item.link) })
            }
        }
    }

}