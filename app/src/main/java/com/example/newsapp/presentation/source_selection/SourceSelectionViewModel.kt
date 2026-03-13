package com.example.newsapp.presentation.source_selection

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.domain.models.NewsSourceItem
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.presentation.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SourceSelectionViewModel @Inject constructor(
    private val repository: NewsRepository
) : BaseViewModel<List<NewsSourceItem>, Unit>() {

    val sources: LiveData<List<NewsSourceItem>> = repository.getNewsSources().asLiveData()

    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent = _errorEvent.asSharedFlow()

    fun toggleSource(source: NewsSourceItem) {
        viewModelScope.launch {
            try {
                repository.toggleSource(source.id, !source.isEnabled)
            } catch (e: Exception) {
                _errorEvent.emit("Ошибка при обновлении источника")
            }
        }
    }
}