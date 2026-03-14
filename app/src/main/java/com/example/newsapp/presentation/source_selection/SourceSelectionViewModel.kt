package com.example.newsapp.presentation.source_selection

import androidx.lifecycle.viewModelScope
import com.example.newsapp.domain.models.NewsSourceItem
import com.example.newsapp.domain.usecases.GetNewsSourcesFlowUseCase
import com.example.newsapp.domain.usecases.ToggleSourceUseCase
import com.example.newsapp.presentation.common.BaseViewModel
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
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent = _errorEvent.asSharedFlow()

    fun toggleSource(source: NewsSourceItem) {
        viewModelScope.launch(exceptionHandler) {
            try {
                toggleSourceUseCase(source.id, !source.isEnabled)
            } catch (e: Exception) {
                _errorEvent.emit("Ошибка при обновлении источника: ${e.localizedMessage}")
            }
        }
    }
}