package com.example.newsapp.feature.news.presentation

import androidx.lifecycle.viewModelScope
import com.example.newsapp.core.common.result.onFailure
import com.example.newsapp.core.common.util.TIMEOUT_PAUSE
import com.example.newsapp.core.common.viewmodel.BaseViewModel
import com.example.newsapp.core.domain.usecase.GetNewsFlowUseCase
import com.example.newsapp.core.domain.usecase.RefreshNewsUseCase
import com.example.newsapp.core.ui.util.UiText
import com.example.newsapp.core.ui.util.toReadableText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(
    getNewsFlowUseCase: GetNewsFlowUseCase,
    private val refreshNewsUseCase: RefreshNewsUseCase
) : BaseViewModel<NewsListViewState, NewsListEvent>() {

    val _isRefreshing = MutableStateFlow(false)
    private val _errorState = MutableStateFlow<UiText?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val uiStateFlow: StateFlow<NewsListViewState> = combine(
        getNewsFlowUseCase(),
        _isRefreshing,
        _errorState
    ) { newsList, refreshing, error ->
        when {
            newsList.isEmpty() && !refreshing && error != null ->
                NewsListViewState.Error(error)

            newsList.isEmpty() && refreshing ->
                NewsListViewState.Loading

            else -> NewsListViewState.Success(
                news = newsList,
                isRefreshing = refreshing
            )
        }
    }.distinctUntilChanged().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_PAUSE),
        initialValue = NewsListViewState.Loading
    )

    init {
        refreshNews()
    }

    fun refreshNews() {
        if (_isRefreshing.value) return

        viewModelScope.launch(exceptionHandler) {
            _errorState.value = null
            _isRefreshing.value = true

            refreshNewsUseCase().onFailure { domainError ->
                val errorText = domainError.toReadableText()

                if (uiStateFlow.value.currentNews.isEmpty()) {
                    _errorState.value = errorText
                } else {
                    NewsListEvent.ShowErrorMessage(errorText).send()
                }
            }
            _isRefreshing.value = false
        }
    }
}
