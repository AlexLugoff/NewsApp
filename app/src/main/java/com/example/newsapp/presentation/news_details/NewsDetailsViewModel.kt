package com.example.newsapp.presentation.news_details

import androidx.lifecycle.viewModelScope
import com.example.newsapp.R
import com.example.newsapp.core.common.BaseViewModel
import com.example.newsapp.core.common.UiText
import com.example.newsapp.core.common.fold
import com.example.newsapp.data.exception.DataError
import com.example.newsapp.domain.usecases.GetNewsDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailsViewModel @Inject constructor(
    private val getNewsDetailsUseCase: GetNewsDetailsUseCase
) : BaseViewModel<NewsDetailsViewState, NewsDetailsEvent>() {

    private val _uiState = MutableStateFlow<NewsDetailsViewState>(NewsDetailsViewState.Loading)
    override val uiStateFlow: StateFlow<NewsDetailsViewState> = _uiState.asStateFlow()

    fun loadNewsDetails(newsLink: String) {
        _uiState.value = NewsDetailsViewState.Loading
        viewModelScope.launch(exceptionHandler) {
            getNewsDetailsUseCase(newsLink).fold(
                onSuccess = { newsItem ->
                    _uiState.value = if (newsItem != null) {
                        NewsDetailsViewState.Success(newsItem)
                    } else {
                        // Это состояние должно быть поймано как DataError.Local.NotFound,
                        // но это дополнительная проверка на случай, если UseCase вернёт Success(null)
                        NewsDetailsViewState.Error(UiText.StringResource(id = R.string.new_not_found))
                    }
                },
                onFailure = { domainError ->
                    val errorMessage = when (domainError) {
                        is DataError.Local.NotFound -> UiText.StringResource(R.string.news_was_not_found_in_the_cache)
                        else -> UiText.StringResource(R.string.error_unknown)
                    }
                    _uiState.value = NewsDetailsViewState.Error(errorMessage)
                }
            )
        }
    }

    fun goToBrowser(url: String) {
        NewsDetailsEvent.GoToBrowser(url = url).send()
    }

    fun shareNews(url: String) {
        NewsDetailsEvent.ShareNews(url).send()
    }
}