package com.example.newsapp.presentation.source_selection

import androidx.lifecycle.viewModelScope
import com.example.newsapp.R
import com.example.newsapp.core.common.viewmodel.BaseViewModel
import com.example.newsapp.core.common.util.TIMEOUT_PAUSE
import com.example.newsapp.core.domain.usecase.GetNewsSourcesFlowUseCase
import com.example.newsapp.core.domain.usecase.ToggleSourceUseCase
import com.example.newsapp.core.model.NewsSourceItem
import com.example.newsapp.core.ui.util.UiText
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
                        R.string.error_source_parser,
                        e.localizedMessage
                    )
                )
            }
        }
    }
}