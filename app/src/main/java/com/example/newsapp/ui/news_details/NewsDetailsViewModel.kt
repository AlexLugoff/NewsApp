package com.example.newsapp.ui.news_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.exception.DataError
import com.example.newsapp.domain.usecases.GetNewsDetailsUseCase
import com.example.newsapp.fold
import com.example.newsapp.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailsViewModel @Inject constructor(
    private val getNewsDetailsUseCase: GetNewsDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<NewsDetailsViewState, NewsDetailsEvent>() {

    private val newsLink: String = checkNotNull(savedStateHandle["newsLink"])

    init {
        loadNewsDetails(newsLink)
    }

    private fun loadNewsDetails(newsLink: String) {
        NewsDetailsViewState.Loading.setValue()
        viewModelScope.launch {
            val sealedResult = getNewsDetailsUseCase(newsLink)

            sealedResult.fold(
                onSuccess = { newsItem ->
                    if (newsItem != null) {
                        NewsDetailsViewState.Success(newsItem).postValue()
                    } else {
                        // Это состояние должно быть поймано как DataError.Local.NOT_FOUND,
                        // но это дополнительная проверка на случай, если UseCase вернёт Success(null)
                        NewsDetailsViewState.Error("Новость не найдена.").postValue()
                    }
                },
                onFailure = { domainError ->
                    val errorMessage = when (domainError) {
                        DataError.Local.NOT_FOUND -> "Новость не найдена в кэше."
                        else -> "Неизвестная ошибка: $domainError"
                    }
                    NewsDetailsViewState.Error(errorMessage).postValue()
                }
            )
        }
    }

    fun goToBrowser(url: String) {
        NewsDetailsEvent.GoToBrowser(url = url).setValue()
    }
}