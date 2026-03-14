package com.example.newsapp.presentation.news

import androidx.lifecycle.viewModelScope
import com.example.newsapp.R
import com.example.newsapp.TIMEOUT_PAUSE_WHEN_FOLDING
import com.example.newsapp.data.exception.DataError
import com.example.newsapp.domain.usecases.GetNewsFlowUseCase
import com.example.newsapp.domain.usecases.RefreshNewsUseCase
import com.example.newsapp.onFailure
import com.example.newsapp.presentation.UniversalText
import com.example.newsapp.presentation.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(
    getNewsFlowUseCase: GetNewsFlowUseCase,
    private val refreshNewsUseCase: RefreshNewsUseCase
) : BaseViewModel<NewsListViewState, NewsListEvent>() {

    init {
        refreshNews()
    }

    override val uiStateFlow: StateFlow<NewsListViewState?> =
        getNewsFlowUseCase()
            .map { newsList -> NewsListViewState.Success(newsList) }
            .distinctUntilChanged()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_PAUSE_WHEN_FOLDING),     // Пауза при сворачивании
                initialValue = NewsListViewState.Loading
            )

    fun refreshNews() {
        if (viewState.value !is NewsListViewState.Success) {
            NewsListViewState.Loading.setValue()
        }

        viewModelScope.launch(exceptionHandler) {
            refreshNewsUseCase().onFailure { domainError ->
                val errorMessage = mapError(domainError)
                NewsListViewState.Error(errorMessage).postValue()
            }
        }
    }

    private fun mapError(error: DataError): UniversalText {
        return when (error) {
            DataError.Network.UNKNOWN_HOST -> UniversalText.Resource(R.string.error_message_unknown_host)
            DataError.Network.CONNECTION_TIMEOUT -> UniversalText.Resource(R.string.error_message_connection_timeout)
            DataError.Local.NOT_FOUND -> UniversalText.Resource(id = R.string.error_message_not_found)
            else -> UniversalText.Resource(R.string.error_message_unknown_error)
        }
    }

    fun onNewsItemClick(newsLink: String) {
        NewsListEvent.NavigateToNewsDetails(newsLink).setValue()
    }
}