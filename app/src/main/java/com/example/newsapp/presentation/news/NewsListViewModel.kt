package com.example.newsapp.presentation.news

import androidx.lifecycle.viewModelScope
import com.example.newsapp.AppDispatchers
import com.example.newsapp.R
import com.example.newsapp.data.exception.DataError
import com.example.newsapp.domain.usecases.GetNewsFlowUseCase
import com.example.newsapp.domain.usecases.RefreshNewsUseCase
import com.example.newsapp.onFailure
import com.example.newsapp.presentation.UniversalText
import com.example.newsapp.presentation.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val getNewsFlowUseCase: GetNewsFlowUseCase,
    private val refreshNewsUseCase: RefreshNewsUseCase,
    private val dispatchers: AppDispatchers
) : BaseViewModel<NewsListViewState, NewsListEvent>() {

    init {
        observeNews()
        refreshNews()
    }

    private fun observeNews() {
        getNewsFlowUseCase.invoke()
            .distinctUntilChanged()
            .onEach { newsList ->
                NewsListViewState.Success(newsList).postValue()
            }
            .flowOn(dispatchers.io)
            .launchIn(viewModelScope)
    }

    fun refreshNews() {
        if (viewState.value !is NewsListViewState.Success) {
            NewsListViewState.Loading.setValue()
        }

        viewModelScope.launch(dispatchers.io) {
            refreshNewsUseCase.invoke().onFailure { domainError ->
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