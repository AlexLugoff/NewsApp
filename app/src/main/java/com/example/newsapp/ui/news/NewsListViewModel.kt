package com.example.newsapp.ui.news

import androidx.lifecycle.viewModelScope
import com.example.newsapp.R
import com.example.newsapp.data.exception.DataError
import com.example.newsapp.domain.usecases.GetNewsListUseCase
import com.example.newsapp.fold
import com.example.newsapp.ui.UniversalText
import com.example.newsapp.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val getNewsListUseCase: GetNewsListUseCase
) : BaseViewModel<NewsListViewState, NewsListEvent>() {

    init {
        loadNews()
    }

    fun loadNews() {
        NewsListViewState.Loading.setValue()

        viewModelScope.launch {
            val sealedResult = getNewsListUseCase()

            sealedResult.fold(
                onSuccess = { newsList ->
                    NewsListViewState.Success(newsList).postValue()
                },
                onFailure = { domainError ->
                    val errorMessage = when (domainError) {
                        DataError.Network.UNKNOWN_HOST -> UniversalText.Resource(id = R.string.error_message_unknown_host)
                        DataError.Network.CONNECTION_TIMEOUT -> UniversalText.Resource(id = R.string.error_message_connection_timeout)
                        DataError.Local.NOT_FOUND -> UniversalText.Resource(id = R.string.error_message_not_found)
                        else -> UniversalText.Resource(id = R.string.error_message_unknown_error)
                    }
                    NewsListViewState.Error(errorMessage).postValue()
                }
            )
        }
    }

    fun onNewsItemClick(newsLink: String) {
        NewsListEvent.NavigateToNewsDetails(newsLink).setValue()
    }
}