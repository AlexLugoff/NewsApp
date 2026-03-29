package com.example.newsapp.feature.details.presentation

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.newsapp.core.model.NewsItem
import com.example.newsapp.core.ui.component.ErrorMessage
import com.example.newsapp.core.ui.component.LoadingIndicator
import com.example.newsapp.core.ui.component.NewsAppBar
import com.example.newsapp.core.ui.util.toAnnotatedString
import com.example.newsapp.feature.details.R
import com.example.newsapp.core.ui.R as UiR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailsContent(
    uiState: NewsDetailsViewState,
    onBackClick: () -> Unit,
    onReadInBrowser: (String) -> Unit,
    onShareClick: (String) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            NewsAppBar(
                title = stringResource(R.string.news_title),
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = {
                    if (uiState is NewsDetailsViewState.Success) {
                        IconButton(onClick = { onShareClick(uiState.newsItem.link) }) {
                            Icon(
                                Icons.Default.Share,
                                contentDescription = stringResource(R.string.share)
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when (uiState) {
                is NewsDetailsViewState.Loading -> LoadingIndicator()

                is NewsDetailsViewState.Success -> {
                    NewsDetailsBody(
                        news = uiState.newsItem,
                        scrollState = scrollState,
                        onReadInBrowser = onReadInBrowser
                    )
                }

                is NewsDetailsViewState.Error -> {
                    ErrorMessage(uiState.message.asString())
                }
            }
        }
    }
}

@Composable
fun NewsDetailsBody(
    news: NewsItem,
    scrollState: ScrollState,
    onReadInBrowser: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        if (!news.imageUrl.isNullOrBlank()) {
            AsyncImage(
                model = news.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
                error = painterResource(UiR.drawable.ic_placeholder),
                placeholder = painterResource(UiR.drawable.ic_placeholder)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Text(
            text = news.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold
        )

        Text(
            text = news.formattedDate,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 0.5.dp)

        val description = news.description
        if (description.isNotBlank()) {
            val annotatedDescription = remember(description) { description.toAnnotatedString() }
            Text(
                text = annotatedDescription,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 28.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { onReadInBrowser(news.link) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.OpenInBrowser, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(stringResource(R.string.read_in_source))
        }
    }
}
