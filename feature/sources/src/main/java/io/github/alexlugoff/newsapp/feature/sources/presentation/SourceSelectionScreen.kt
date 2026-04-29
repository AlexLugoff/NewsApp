package io.github.alexlugoff.newsapp.feature.sources.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.alexlugoff.newsapp.core.common.util.showLongToast
import io.github.alexlugoff.newsapp.core.model.NewsSourceItem
import io.github.alexlugoff.newsapp.feature.sources.presentation.components.SourceItem
import io.github.alexlugoff.newsapp.feature.sources.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SourceSelectionBottomSheetScreen(
    onDismiss: () -> Unit,
    viewModel: SourceSelectionViewModel = hiltViewModel()
) {
    val sources by viewModel.sourcesState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.errorEvent.collect { errorMessage ->
            context.showLongToast(errorMessage.asString(context))
        }
    }

    SourceSelectionContent(
        sources = sources,
        onDismiss = onDismiss,
        onToggleSource = viewModel::toggleSource
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SourceSelectionContent(
    sources: List<NewsSourceItem>,
    onDismiss: () -> Unit,
    onToggleSource: (NewsSourceItem) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            Text(
                text = stringResource(R.string.news_sources_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 450.dp)
            ) {
                items(
                    items = sources,
                    key = { it.id }
                ) { source ->
                    SourceItem(
                        source = source,
                        onToggle = { onToggleSource(source) }
                    )
                }
            }
        }
    }
}