package com.example.newsapp.ui.news

import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.exception.DataError
import com.example.newsapp.domain.usecases.GetNewsListUseCase
import com.example.newsapp.fold
import com.example.newsapp.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val getNewsListUseCase: GetNewsListUseCase
) : BaseViewModel<NewsListViewState, NewsListEvent>() {

    private val _uiState = MutableStateFlow<NewsListViewState>(NewsListViewState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadNews()
    }

    fun loadNews() {
        _uiState.value = NewsListViewState.Loading

        viewModelScope.launch {
            val sealedResult = getNewsListUseCase()

            sealedResult.fold(
                onSuccess = { newsList ->
                    _uiState.value = NewsListViewState.Success(newsList)
                },
                onFailure = { domainError ->
                    val errorMessage = when (domainError) {
                        DataError.Network.UNKNOWN_HOST -> "Ошибка сети: неизвестный хост."
                        DataError.Network.CONNECTION_TIMEOUT -> "Ошибка сети: превышен таймаут."
                        DataError.Local.NOT_FOUND -> "Данные не найдены, даже в кэше."
                        else -> "Неизвестная ошибка загрузки данных."
                    }
                    _uiState.value = NewsListViewState.Error(errorMessage)
                }
            )
        }
    }

    fun onNewsItemClicked(newsLink: String) {
        // TODO: Логика навигации к экрану деталей с использованием newsLink
    }
}