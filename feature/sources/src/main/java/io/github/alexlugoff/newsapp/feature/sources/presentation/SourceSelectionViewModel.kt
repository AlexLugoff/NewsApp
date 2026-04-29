package io.github.alexlugoff.newsapp.feature.sources.presentation

import androidx.lifecycle.viewModelScope
import io.github.alexlugoff.newsapp.core.common.util.TIMEOUT_PAUSE
import io.github.alexlugoff.newsapp.core.common.viewmodel.BaseViewModel
import io.github.alexlugoff.newsapp.core.domain.usecase.GetNewsSourcesFlowUseCase
import io.github.alexlugoff.newsapp.core.domain.usecase.ToggleSourceUseCase
import io.github.alexlugoff.newsapp.core.model.NewsSourceItem
import io.github.alexlugoff.newsapp.core.ui.util.UiText
import io.github.alexlugoff.newsapp.feature.sources.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SourceSelectionViewModel @Inject constructor(
    getNewsSourcesUseCase: GetNewsSourcesFlowUseCase,
    private val toggleSourceUseCase: ToggleSourceUseCase
) : BaseViewModel<SourceSelectionViewState, SourceSelectionEvent>() {

    val sourcesState: StateFlow<List<NewsSourceItem>> = getNewsSourcesUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_PAUSE),
            initialValue = emptyList()
        )

    private val _errorEvent = MutableSharedFlow<UiText>()
    val errorEvent = _errorEvent.asSharedFlow()

    fun toggleSource(source: NewsSourceItem) {
        viewModelScope.launch(exceptionHandler) {
            try {
                toggleSourceUseCase(source.id, !source.isEnabled)
            } catch (e: Exception) {
                _errorEvent.emit(
                    UiText.StringResource(
                        R.string.error_parser,
                        e.localizedMessage ?: ""
                    )
                )
            }
        }
    }
}
