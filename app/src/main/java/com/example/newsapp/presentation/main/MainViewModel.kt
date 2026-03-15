package com.example.newsapp.presentation.main

import androidx.lifecycle.viewModelScope
import com.example.newsapp.domain.usecases.ClearOldNewsUseCase
import com.example.newsapp.presentation.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val clearOldNewsUseCase: ClearOldNewsUseCase
) : BaseViewModel<MainViewState, MainEvent>() {
    init {
        viewModelScope.launch(exceptionHandler) {
            clearOldNewsUseCase.invoke()
        }
    }
}