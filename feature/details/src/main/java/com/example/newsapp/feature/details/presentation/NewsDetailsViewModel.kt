package com.example.newsapp.feature.details.presentation

import androidx.lifecycle.viewModelScope
import com.example.newsapp.core.common.error.DataError
import com.example.newsapp.core.common.result.fold
import com.example.newsapp.core.common.viewmodel.BaseViewModel
import com.example.newsapp.core.domain.usecase.GetNewsDetailsUseCase
import com.example.newsapp.core.ui.R
import com.example.newsapp.core.ui.util.UiText
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
                        NewsDetailsViewState.Error(UiText.StringResource(id = R.string.error_unknown))
                    }
                },
                onFailure = { domainError ->
                    val errorMessage = when (domainError) {
                        is DataError.Local.NotFound -> UiText.StringResource(R.string.error_local_not_found)
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
