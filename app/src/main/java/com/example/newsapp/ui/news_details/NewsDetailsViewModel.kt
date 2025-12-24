package com.example.newsapp.ui.news_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.newsapp.R
import com.example.newsapp.data.exception.DataError
import com.example.newsapp.domain.usecases.GetNewsDetailsUseCase
import com.example.newsapp.fold
import com.example.newsapp.ui.UniversalText
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
                        NewsDetailsViewState.Error(UniversalText.Resource(id = R.string.new_not_found)).postValue()
                        UniversalText.Resource(id = R.string.unknown_error)
                    }
                },
                onFailure = { domainError ->
                    val errorMessage = when (domainError) {
                        DataError.Local.NOT_FOUND -> UniversalText.Resource(id = R.string.news_was_not_found_in_the_cache)
                        else -> UniversalText.Resource(id = R.string.unknown_error, domainError.toString())
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