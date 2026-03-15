package com.example.newsapp.presentation.news

import androidx.lifecycle.viewModelScope
import com.example.newsapp.R
import com.example.newsapp.data.exception.DataError
import com.example.newsapp.domain.usecases.GetNewsFlowUseCase
import com.example.newsapp.domain.usecases.RefreshNewsUseCase
import com.example.newsapp.extensions.onFailure
import com.example.newsapp.presentation.UniversalText
import com.example.newsapp.presentation.common.BaseViewModel
import com.example.newsapp.presentation.common.CommonEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val getNewsFlowUseCase: GetNewsFlowUseCase,
    private val refreshNewsUseCase: RefreshNewsUseCase
) : BaseViewModel<NewsListViewState, NewsListEvent>() {

    private val refreshTrigger = MutableSharedFlow<Unit>(replay = 1)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val uiStateFlow: StateFlow<NewsListViewState> = refreshTrigger
        .onStart { emit(Unit) }
        .flatMapLatest {
            getNewsFlowUseCase()
        }
        .map { newsList ->
            if (newsList.isEmpty()) NewsListViewState.Loading
            else NewsListViewState.Success(newsList)
        }
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NewsListViewState.Loading
        )

    init {
        refreshNews()
    }

    fun refreshNews() {
        viewModelScope.launch(exceptionHandler) {
            refreshNewsUseCase().onFailure { domainError ->
                // Если сеть упала, а в базе пусто — тогда показываем экран ошибки
                val currentNews = getNewsFlowUseCase().first()
                if (currentNews.isEmpty()) {
                    NewsListViewState.Error(mapError(domainError)).postValue()
                } else {
                    CommonEvent.ShowLongToast(mapError(domainError).toString()).postValue()
                }
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