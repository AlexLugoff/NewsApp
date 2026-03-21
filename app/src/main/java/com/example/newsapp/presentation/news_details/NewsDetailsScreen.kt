package com.example.newsapp.presentation.news_details

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.newsapp.extensions.toAnnotatedString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailsScreen(
    newsLink: String,
    onBackClick: () -> Unit,
    viewModel: NewsDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Аналог onViewCreated: загружаем данные при входе на экран
    LaunchedEffect(newsLink) {
        viewModel.loadNewsDetails(newsLink)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Новость", maxLines = 1, overflow = TextOverflow.Ellipsis) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
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
            when (val state = uiState) {
                is NewsDetailsViewState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is NewsDetailsViewState.Success -> {
                    val news = state.newsItem
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        if (!news.imageUrl.isNullOrBlank()) {
                            AsyncImage(
                                model = news.imageUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        Text(
                            text = news.title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = news.formattedDate,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        val description = news.description
                        if (description.isNotBlank()) {
                            val annotatedDescription = remember(description) {
                                description.toAnnotatedString()
                            }

                            Text(
                                text = annotatedDescription,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = { viewModel.goToBrowser(news.link) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Читать в браузере")
                        }
                    }
                }

                is NewsDetailsViewState.Error -> {
                    Text(
                        text = state.message.asString(context),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }

                else -> Unit
            }
        }
    }

    // Обработка разовых событий (Navigation)
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            if (event is NewsDetailsEvent.GoToBrowser) {
                val intent = Intent(Intent.ACTION_VIEW, event.url.toUri())
                context.startActivity(intent)
            }
        }
    }
}